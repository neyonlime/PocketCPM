package com.brewdevelopment.pocketcpm

/**
 * Created by neyonlime on 2017-08-15.
 */

class Project(name: String){
    lateinit var ID: String
    var taskList: ArrayList<Task> = ArrayList()
}