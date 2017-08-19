package com.brewdevelopment.pocketcpm

/**
 * Created by neyonlimell on 2017-08-15.
 */
class Champion(var name: String){
    var ID: Long = -1                                   //-1 = not added to database
    var assignedTasks: ArrayList<Task> = ArrayList()

}