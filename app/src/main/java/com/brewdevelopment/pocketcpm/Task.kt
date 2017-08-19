package com.brewdevelopment.pocketcpm

import android.content.ContentValues

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task(){
    private lateinit var pred: Task     //the task preceding the current
    private lateinit var depend: Task   //the task depending on the current

    var ID: Long = -1                           //-1 means not added to database
    lateinit var attribute: ContentValues       //this is already parcelable


    fun getDuration(): Float{
        var duration: Float = 0f
        //find duration
        return  duration

    }

    companion object {
        val NAME_COLUMN = "name"
        val CHAMPION_COLUMN = "champion"
        val START_COLUMN = "start"
        val END_COLUMN = "end"
        val PREDECESSOR_COLUMN = "predecessor"
        val DEPENDENT_COLUMN = "dependent"
    }

}