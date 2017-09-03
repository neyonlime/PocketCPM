package com.brewdevelopment.pocketcpm

import android.content.ContentValues

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task(){
    private var pred: ArrayList<Task>    //the task preceding the current
    private var depend: ArrayList<Task>   //the task depending on the current
    private var champion: Champion


    private val EMPTY: Long = -1
    var ID: Long = EMPTY                           //-1 means not added to database
    var attribute: ContentValues       //this is already parcelable

    init {
        attribute = ContentValues()

        pred = ArrayList()
        depend = ArrayList()
        champion = Champion()

    }


    companion object {
        val NAME_COLUMN = "name"
        val DESCRIPTION_COLUMN = "description"
        val CHAMPION_COLUMN = "champion"
        val DURATION_COLUMN = "duration"
        val PREDECESSOR_COLUMN = "predecessor"
        val DEPENDENT_COLUMN = "dependent"
    }

    private fun addPred(task: Task){
        if(task.ID != EMPTY){
            pred.add(task)
            attribute.put(PREDECESSOR_COLUMN, getPredList())
            task.addDependent(this)
        }else{
            attribute.put(PREDECESSOR_COLUMN, "")
        }
    }

    fun removePred(task: Task){
        if(task.ID != EMPTY){
            pred.remove(task)
            attribute.put(PREDECESSOR_COLUMN, getPredList())
            removePred(task)
        }
    }

    fun getPredList(): String{
        var list = String()
        for(task in pred){
            list += "," + task.ID
        }

        if(list.isNotEmpty()){
            list = list.substring(1)
        }

        return list
    }


    fun getDependList(): String{
        var list = String()
        for(task in depend){
            list += "," + task.ID
        }

        if(list.isNotEmpty()){
            list = list.substring(1)
        }

        return list
    }

    fun addDependent(task: Task){
        depend.add(task)
        attribute.put(DEPENDENT_COLUMN, getDependList())
    }

    fun removeDependent(task: Task){
        depend.remove(task)
        attribute.put(DEPENDENT_COLUMN, getDependList())
    }

    fun getPred(): ArrayList<Task>{
        return pred
    }

    fun getDepend(): ArrayList<Task>{
        return depend
    }

    fun setPred(list: ArrayList<Task>){
        for(task in list){
            addPred(task)
        }
    }

    fun setDepend(list: ArrayList<Task>){
        for(task in list){
            addDependent(task)
        }
    }

    fun setChampion(champion: Champion){
        this.champion = champion
    }

    fun getChampion(index: Int): Champion{
        return champion
    }
}