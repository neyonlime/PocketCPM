package com.brewdevelopment.pocketcpm

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task(){
    private lateinit var pred: Task     //the task preceding the current
    private lateinit var depend: Task   //the task depending on the current

    lateinit var ID: String
    lateinit var attribute: HashMap<String, String>

}