package com.brewdevelopment.pocketcpm

/**
 * Created by neyonlime on 2017-08-15.
 */

class Project(){

    lateinit var name: String
    var ID: Long = -1       //if id == -1 then the project has not been added to the database
    var taskList: ArrayList<Task>
    var start: String


    constructor(name: String): this(){
        this.name = name
    }

    init {
        taskList = ArrayList()
        start = ""
    }


    fun getTaskList(): String{
        var taskList = ""
        for(task in this.taskList){
            taskList+="," + task.ID
        }

        if(taskList.length > 0){
            taskList = taskList.substring(1)
        }

        return taskList
    }
    fun getTOC(): Int{
        var max= 0
        for(i in taskList){
            if(CritCalc(i, Project(this.name)).getEarlyFinish()>0){
            max=CritCalc(i,Project(this.name)).getEarlyFinish()}
        }
        return max
    }
}
