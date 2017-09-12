package com.brewdevelopment.pocketcpm

import android.content.Context

/**
 * Created by neyonlimell on 2017-08-15.
 */

class Champion(){
    constructor(name: String): this(){
        this.name = name
    }
    var ID: Long = -1                                   //-1 = not added to database
    lateinit var name: String
    var assignedTasks: ArrayList<Task> = ArrayList()

    fun getTaskList(): String{
        var taskList = ""
        for(task in assignedTasks){
            taskList += "," + task.ID
        }
        if(taskList.isNotEmpty()){
            taskList = taskList.substring(1)
        }

        return taskList
    }

    fun delete(context: Context){
        val fragmentEventListener = context as FragmentEventListener
        fragmentEventListener.onDelete(this)
    }

}