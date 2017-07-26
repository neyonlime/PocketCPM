package com.brewdevelopment.pocketcpm

/**
 * Created by neyon on 2017-07-20.
 */
class Task constructor(private var name: String){
    private lateinit var pred: Task
    private lateinit var depend: Task
    private lateinit var tag: String
    private lateinit var desc: String   //may change with versions to be a description object
    private lateinit var champion: String   //consider Champion object that contains basice infortamtino about the person in charge of completeing this task

    init {
        //any inizialization code
    }

    constructor(name: String, tag: String, desc: String) : this(name){

    }

}