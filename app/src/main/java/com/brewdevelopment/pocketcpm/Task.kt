package com.brewdevelopment.pocketcpm

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task constructor(private var name: String){
    private lateinit var pred: Task     //the task preceding the current
    private lateinit var depend: Task   //the task depending on the current
    private lateinit var tag: String    //a tag associated with the department to which the tag belongs (NO USECASE)
    private lateinit var desc: String   //may change with versions to be a description object
    private lateinit var champion: String   //consider Champion object that contains basice infortamtino about the person in charge of completeing this task

    init {
        //any inizialization code
    }

    constructor(name: String, tag: String, desc: String) : this(name){

    }

}