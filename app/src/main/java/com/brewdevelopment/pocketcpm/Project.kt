package com.brewdevelopment.pocketcpm

/**
 * Created by neyonlime on 2017-08-15.
 */

class Project(){

    lateinit var name: String
    var ID: Long = -1       //if id == -1 then the project has not been added to the database
    var taskList: ArrayList<Task>

    constructor(name: String): this(){
        this.name = name
    }

    init {
        taskList = ArrayList()
    }




    //Helper
    fun getTotalTime(): Float{
        var totalTime: Float = 0f
        for(task in taskList){
            totalTime+= task.getDuration()
        }
        return totalTime
    }

}