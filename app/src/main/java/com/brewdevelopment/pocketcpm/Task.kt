package com.brewdevelopment.pocketcpm

import android.content.ContentValues

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task(){
    private lateinit var pred: Task     //the task preceding the current
    private lateinit var depend: Task   //the task depending on the current

    private val EMPTY: Long = -1
    var ID: Long = EMPTY                           //-1 means not added to database
    var attribute: ContentValues       //this is already parcelable

    init {
        attribute = ContentValues()
    }

    fun getDuration(): Float{
        var duration: Float = 0f
        //find duration
        return  duration

    }

    companion object {
        val NAME_COLUMN = "name"
        val DESCRIPTION_COLUMN = "description"
        val CHAMPION_COLUMN = "champion"
        val START_COLUMN = "start"
        val END_COLUMN = "end"
        val PREDECESSOR_COLUMN = "predecessor"
        val DEPENDENT_COLUMN = "dependent"
    }

    fun setPred(task: Task){
        if(task.ID != EMPTY){
            pred = task
            attribute.put(PREDECESSOR_COLUMN, task.ID)
            task.setDependent(this)
        }else{
            attribute.put(PREDECESSOR_COLUMN, "")
        }
    }

    fun setDependent(task: Task){
        depend = task
        attribute.put(DEPENDENT_COLUMN, task.ID)
    }



}