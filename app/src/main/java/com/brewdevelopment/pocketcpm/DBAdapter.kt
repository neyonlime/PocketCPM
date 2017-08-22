package com.brewdevelopment.pocketcpm

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.provider.BaseColumns
import android.util.Log
import java.sql.SQLException

/**
 * Created by neyonlime on 2017-08-16.
 */
class DBAdapter(dbName: String, context: Context){

    companion object {
        private lateinit var db: SQLiteDatabase
        private lateinit var mDBManager: com.brewdevelopment.pocketcpm.DBAdapter.DBManager

        val READ = "read"               //only for reading
        val WRITE= "write"              //for reading and writing

        //debugging
        val PROJECT_ADDED = "project_add"
    }
    
    init {
        mDBManager = DBManager(context, "pocketcpm")        //instance of database manager
    }

    //used to open the database
    @Throws(SQLException::class)
    fun open(request: String){
        //open the data base
        if(!db.isOpen && db !== null){
            OpenDatabaseTask().execute(request)
        }else if(db.isReadOnly&&request == WRITE && db.isOpen){
            db.close()
            OpenDatabaseTask().execute(request)
        }
    }


    //ASYNC TASK
    private class OpenDatabaseTask: AsyncTask<String, Boolean, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            for(request in params){
                if(request == com.brewdevelopment.pocketcpm.DBAdapter.WRITE){
                    //get readable database
                    db = mDBManager.writableDatabase
                    if(db.isOpen){
                        return true // was opened
                    }

                }else if(request == com.brewdevelopment.pocketcpm.DBAdapter.READ){
                    //get writeable database
                    db = mDBManager.readableDatabase
                    if(db.isOpen){
                       return true //was opened
                    }
                }
            }
            return false    //was not opened
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
        }
    }

    //deletes the specified table
    fun deleteTable(tbName: String){
        val SQL_DELETE_ENTRIES: String =
                "DROP TABLE IF EXISTS " + tbName;
    }

    fun checkDBState(){
        if(db === null || !db.isOpen){
            throw IllegalStateException("Database is not open")
        }
    }

    //save to database
    fun save(obj: Any){ //takes in any object, only performs functions with Project, Task, and Champion obj
        when(obj){
            is Project -> {
                //save a project
                open(WRITE)             //opens the database
                checkDBState()          //checks that the database is open and ready for print
                var values = ContentValues()
                var taskList = ""
                for(task in obj.taskList){
                    taskList+=","+task.ID
                    Log.i(PROJECT_ADDED, taskList)
                }

                taskList = taskList.substring(1)

                values.put(DBManager.Contract.ProjectTable.NAME_COLUMN, obj.name)
                values.put(DBManager.Contract.ProjectTable.TASK_LIST_COLUMN, taskList)
                values.put(DBManager.Contract.ProjectTable.TOTAL_TIME_COLUMN, obj.getTotalTime())
                obj.ID = db.insert(DBManager.Contract.ProjectTable.TABLE_NAME, null, values)

            }
            is Task -> {
                //save task
                open(WRITE)
                checkDBState()
                var values = obj.attribute
                obj.ID = db.insert(DBManager.Contract.TaskTable.TABLE_NAME, null,values)

            }
            is Champion -> {
                //save champion
                open(WRITE)
                checkDBState()
                var taskList = ""
                for(task in obj.assignedTasks){
                    taskList += "," + task.ID               //id1,id2,id3, ...
                }
                taskList = taskList.substring(1)            //gets rid of the first comma

                var values = ContentValues()
                values.put(DBManager.Contract.ChampionTable.NAME_COLUMN, obj.name)
                values.put(DBManager.Contract.ChampionTable.TASKS_COLUMN, taskList)

                obj.ID = db.insert(DBManager.Contract.ChampionTable.TABLE_NAME,null,values)
            }
            else -> {}
        }
    }

    //Accessors
    fun getProjects(): ArrayList<Project> {
        open(READ)

        var projections = arrayOf(DBManager.Contract.ProjectTable.ID, DBManager.Contract.ProjectTable.NAME_COLUMN)
        checkDBState()

        var sortOrder = DBManager.Contract.ProjectTable.NAME_COLUMN+ " DESC"
        var cursor: Cursor = db.query(DBManager.Contract.ProjectTable.TABLE_NAME, projections,null,null, null, null, sortOrder)

        var projects: ArrayList<Project> = ArrayList()
        while(cursor.moveToNext()){
            var name: String = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.ProjectTable.NAME_COLUMN))
            var project = Project(name)

            project.ID = cursor.getLong(cursor.getColumnIndexOrThrow(DBManager.Contract.ProjectTable.ID))
            projects.add(project)
        }
        cursor.close()
        return projects
    }

    fun getTaskList(id: Long): ArrayList<Task> {
        open(READ)

        var projections = arrayOf(DBManager.Contract.ProjectTable.ID, DBManager.Contract.ProjectTable.TASK_LIST_COLUMN)
        checkDBState()
        var selection = DBManager.Contract.ProjectTable.ID
        val selectionArgs = arrayOf(id as String)

        var sortOrder = DBManager.Contract.ProjectTable.ID + " DESC"

        var cursor: Cursor = db.query(DBManager.Contract.ProjectTable.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder)

        var taskIds = ""
        while(cursor.moveToNext()){
            //should only loop once
            taskIds = cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.ProjectTable.TASK_LIST_COLUMN))
        }
        cursor.close()
        var ids = taskIds.split(',')       //array of the ids (String)

        var taskList = ArrayList<Task>()
        for(id in ids){
            taskList.add(getTaskById(id))
        }

        return taskList
    }

    fun getTaskById(id: String): Task{
        checkDBState()

        var projections = arrayOf(DBManager.Contract.TaskTable.ID, DBManager.Contract.TaskTable.NAME_COLUMN,
                                    DBManager.Contract.TaskTable.CHAMPION_COLUMN, DBManager.Contract.TaskTable.START_COLUMN,
                                    DBManager.Contract.TaskTable.END_COLUMN, DBManager.Contract.TaskTable.PREDECESSOR_COLUMN,
                                    DBManager.Contract.TaskTable.DEPENDENT_COLUMN)

        var selection = DBManager.Contract.TaskTable.ID
        var selectionArgs = arrayOf(id)

        var sortOrder = DBManager.Contract.TaskTable.TABLE_NAME + " DESC"


        var cursor: Cursor = db.query(DBManager.Contract.TaskTable.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder)

        var task = Task()
        while(cursor.moveToNext()){
            task.ID = cursor.getLong(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.ID))
            task.attribute.put(Task.NAME_COLUMN,cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.NAME_COLUMN)))
            task.attribute.put(Task.CHAMPION_COLUMN,cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.CHAMPION_COLUMN)))
            task.attribute.put(Task.START_COLUMN, cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.START_COLUMN)))
            task.attribute.put(Task.END_COLUMN, cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.END_COLUMN)))
            task.attribute.put(Task.PREDECESSOR_COLUMN, cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.PREDECESSOR_COLUMN)))
            task.attribute.put(Task.DEPENDENT_COLUMN, cursor.getString(cursor.getColumnIndexOrThrow(DBManager.Contract.TaskTable.DEPENDENT_COLUMN)))
        }

        return task
    }


    private class DBManager(context: Context, dbName: String): SQLiteOpenHelper(context, dbName, null, VERSION){

        //schema definition
        companion object Contract {
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
    }

}