package com.brewdevelopment.pocketcpm

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

/**
 * Created by ashkanabedian on 2017-08-23.
 */
class  DataProvider(): ContentProvider(){

    companion object {

        private val AUTHORITY: String = "com.brewdevelopment.pocketcpm.dataprovider"
        private val BASE_PATH: String = "data"
        private val PROJECT_PATH = "projects"
        private val TASK_PATH = "tasks"
        private val CHAMPION_PATH = "champions"
        val PROJECT_URI: Uri = Uri.parse("content://${AUTHORITY}/${PROJECT_PATH}")
        val TASK_URI: Uri = Uri.parse("content://${AUTHORITY}/${TASK_PATH}")
        val CHAMPION_URI: Uri = Uri.parse("content://${AUTHORITY}/${CHAMPION_PATH}")

        //Requsted operation
        //the integer values associated with each path
        private val PROJECTS = 0
        private val TASKS = 1
        private val CHAMPIONS = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, PROJECT_PATH, PROJECTS)
            uriMatcher.addURI(AUTHORITY, "${TASK_PATH}/#", TASKS)
            uriMatcher.addURI(AUTHORITY, CHAMPION_PATH, CHAMPIONS)
        }
    }

    lateinit private var db: SQLiteDatabase
    override fun onCreate(): Boolean {
        val helper = DBOpenHelper(context)
        db = helper.writableDatabase
        return true
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {

        when(uriMatcher.match(uri)){
            PROJECTS -> {

                return db.query(DBOpenHelper.Companion.ProjectTable.TABLE_NAME, projection,selection, selectionArgs, null,null, sortOrder)
            }
            TASKS -> {
                return db.query(DBOpenHelper.Companion.TaskTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            CHAMPIONS -> {
                return db.query(DBOpenHelper.Companion.ChampionTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
        }
        return throw Exception("NOT GOOD")
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}