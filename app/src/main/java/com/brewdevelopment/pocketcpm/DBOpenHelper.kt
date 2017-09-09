package com.brewdevelopment.pocketcpm

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * Created by ashkanabedian on 2017-08-23.
 */
class DBOpenHelper(context: Context):SQLiteOpenHelper(context, DB_NAME, null, VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        createTable(db, ProjectTable.TABLE_NAME)
        createTable(db, TaskTable.TABLE_NAME)
        createTable(db, ChampionTable.TABLE_NAME)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createTable(db: SQLiteDatabase?, table: String){
        when(table){
            ProjectTable.TABLE_NAME -> {
                val CREATE_SQL_ENTERIES = "CREATE TABLE ${ProjectTable.TABLE_NAME} (" +
                        "${ProjectTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${ProjectTable.NAME_COLUMN} TEXT, " +
                        "${ProjectTable.TASK_LIST_COLUMN} TEXT, ${ProjectTable.TOTAL_TIME_COLUMN} FLOAT)"
                db!!.execSQL(CREATE_SQL_ENTERIES)
            }
            TaskTable.TABLE_NAME ->{
                val CREATE_SQL_ENTERIES = "CREATE TABLE ${TaskTable.TABLE_NAME} (" +
                        "${TaskTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${TaskTable.NAME_COLUMN} TEXT," +
                        "${TaskTable.DESCRIPTION_COLUMN} TEXT," +
                        "${TaskTable.CHAMPION_COLUMN} TEXT, ${TaskTable.START_COLUMN} TEXT," +
                        "${TaskTable.END_COLUMN} TEXT, ${TaskTable.PREDECESSOR_COLUMN} TEXT " +
                        "${TaskTable.DEPENDENT_COLUMN} TEXT)"
                db!!.execSQL(CREATE_SQL_ENTERIES)
            }
            ChampionTable.TABLE_NAME ->{
                val CREATE_SQL_ENTERIES = "CREATE TABLE ${ChampionTable.TABLE_NAME} (" +
                        "${ChampionTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${ChampionTable.NAME_COLUMN} TEXT, " +
                        "${ChampionTable.TASKS_COLUMN} TEXT)"
                db!!.execSQL(CREATE_SQL_ENTERIES)
            }
        }
    }


    companion object {
        var VERSION = 1   //must be incremented if the schema is changed
        var DB_NAME = "data.db"   //must be incremented if the schema is changed

        var ALL_PROJECTS = arrayOf(ProjectTable.ID, ProjectTable.NAME_COLUMN, ProjectTable.TOTAL_TIME_COLUMN)
        var ALL_TASKS = arrayOf(TaskTable.ID, TaskTable.NAME_COLUMN, TaskTable.DESCRIPTION_COLUMN,
                TaskTable.CHAMPION_COLUMN, TaskTable.START_COLUMN, TaskTable.END_COLUMN,
                TaskTable.PREDECESSOR_COLUMN, TaskTable.DEPENDENT_COLUMN)
        var ALL_CHAMPIONS = arrayOf(ChampionTable.ID, ChampionTable.NAME_COLUMN, ChampionTable.TASKS_COLUMN)

        object ProjectTable: BaseColumns{
            val TABLE_NAME = "projects"
            val ID: String = "_id"
            val NAME_COLUMN = "name"
            val TASK_LIST_COLUMN = "task list"
            val TOTAL_TIME_COLUMN = "total time"
        }
        object TaskTable: BaseColumns{
            val TABLE_NAME = "tasks"
            val ID: String = "_id"
            val NAME_COLUMN = "name"
            val DESCRIPTION_COLUMN = "description"
            val CHAMPION_COLUMN = "champion"
            val START_COLUMN = "start"
            val END_COLUMN = "end"
            val PREDECESSOR_COLUMN = "predecessor"
            val DEPENDENT_COLUMN = "dependent"
        }
        object ChampionTable{
            val TABLE_NAME = "champions"
            val ID: String = "_id"
            val NAME_COLUMN = "name"
            val TASKS_COLUMN = "tasks"
        }
    }
}