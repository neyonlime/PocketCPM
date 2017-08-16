package com.brewdevelopment.pocketcpm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.provider.BaseColumns
import java.sql.SQLException

/**
 * Created by neyonlime on 2017-08-16.
 */
class DBAdapter(dbName: String, context: Context){

    private lateinit var openTask: OpenDatabaseTask

    companion object {
        private lateinit var db: SQLiteDatabase
        private lateinit var mDBManager: com.brewdevelopment.pocketcpm.DBAdapter.DBManager

        val READ = "read"
        val WRITE= "write"
    }
    
    init {
        mDBManager = DBManager(context, "pocketcpm")
    }

    //used to open the database
    @Throws(SQLException::class)
    fun open(request: String){
        //open the data base
        if(!db.isOpen && db !== null){
            OpenDatabaseTask().execute(request)
        }
    }


    //ASYNC TASK
    private class OpenDatabaseTask: AsyncTask<String, Boolean, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            for(request in params){
                if(request == com.brewdevelopment.pocketcpm.DBAdapter.WRITE){
                    //get readable database
                    db = mDBManager.writableDatabase

                }else if(request == com.brewdevelopment.pocketcpm.DBAdapter.READ){
                    //get writeable database
                    db = mDBManager.readableDatabase
                }
            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
        }
    }




    private class DBManager(context: Context, dbName: String): SQLiteOpenHelper(context, dbName, null, VERSION){

        //schema definition
        companion object {
            var VERSION = 1   //must be incremented if the schema is changed

            object ProjectTable: BaseColumns {
                val TABLE_NAME = "projects"
                val ID: String = "_id"
                val NAME_COLUMN = "name"
                val TASK_LIST_COLUMN = "task list"
                val TOTAL_TIME_COLUMN = "total time"
            }

            object TaskTable{
                val TABLE_NAME = "tasks"
                val ID: String = "_id"
                val NAME_COLUMN = "name"
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


        override fun onCreate(db: SQLiteDatabase?) {
            //create the table in the respective method 'createProjectTable()'
            //automatically creates a tasks table and a projects table  and a champion table
            createTable(db, ProjectTable.TABLE_NAME)
            createTable(db, TaskTable.TABLE_NAME)
            createTable(db, ChampionTable.TABLE_NAME)


        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            //define an ubgrade policy in the case that a new version is pushed
            //this is offline and maintains no links so just recreaete the database
            TODO("Implement the upgrade method")
        }

        //called to create the project table
        fun createTable(db: SQLiteDatabase?, table: String){
            when (table){


                ProjectTable.TABLE_NAME -> {
                    val CREATE_SQL_ENTERIES = "CREATE TABLE if not exist ${ProjectTable.TABLE_NAME} (" +
                            "${ProjectTable.ID} INTEGER PRIMARY KEY, ${ProjectTable.NAME_COLUMN} TEXT, " +
                            "${ProjectTable.TASK_LIST_COLUMN} TEXT, ${ProjectTable.TOTAL_TIME_COLUMN} FLOAT)"
                    db!!.execSQL(CREATE_SQL_ENTERIES)
                }
                TaskTable.TABLE_NAME -> {
                    val CREATE_SQL_ENTERIES = "CREATE TABLE if not exist ${TaskTable.TABLE_NAME} (" +
                            "${TaskTable.ID} INTEGER PRIMARY KEY, ${TaskTable.NAME_COLUMN} TEXT, " +
                            "${TaskTable.CHAMPION_COLUMN} TEXT, ${TaskTable.START_COLUMN} TEXT," +
                            "${TaskTable.END_COLUMN} TEXT, ${TaskTable.PREDECESSOR_COLUMN} TEXT " +
                            "${TaskTable.DEPENDENT_COLUMN} TEXT)"
                    db!!.execSQL(CREATE_SQL_ENTERIES)
                }

                ChampionTable.TABLE_NAME -> {
                    val CREATE_SQL_ENTERIES = "CREATE TABLE if not exist ${ChampionTable.TABLE_NAME} (" +
                            "${ChampionTable.ID} INTEGER PRIMARY KEY, ${ChampionTable.NAME_COLUMN} TEXT, " +
                            "${ChampionTable.TASKS_COLUMN} TEXT)"
                    db!!.execSQL(CREATE_SQL_ENTERIES)
                }
            }
        }

        //deletes the specified table
        fun deleteTable(tbName: String){
            val SQL_DELETE_ENTRIES: String =
                    "DROP TABLE IF EXISTS " + tbName;
        }

        //save to database
        fun save(project: Project){
            //save a new project NOT used for appending tasks to current project
        }

        fun save(task: Task){
            //save new tasks and associate them with projects
        }

        fun save(champion: Champion){
            //svae new championsg
        }


    }

}