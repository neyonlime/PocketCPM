package com.brewdevelopment.pocketcpm

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

}