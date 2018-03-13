package com.brewdevelopment.pocketcpm

import android.content.ContentValues
import android.content.Context
import android.util.Log

/**
 * Created by neyon on 2017-07-20.
 * OBJECT/ a task is something that the user wishes to complete, the have relationships between eachother
 */
class Task() {
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
            Log.d("add_dependent", "adding dependent ${this.ID} as dependent on ${task.ID}}")
            task.addDependent(this)
            Log.d("add_dependent", "${this.ID} added as dependent to ${task.ID}, the dependent list: ${task.getDependList()}")
        }else{
            attribute.put(PREDECESSOR_COLUMN, "")
        }
    }

    fun removePred(task: Task){
        if(task.ID != EMPTY){
            pred.remove(task)
            attribute.put(PREDECESSOR_COLUMN, getPredList())

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
        if(champion.ID != EMPTY){
            this.champion = champion
            champion.assignedTasks.add(this)
            attribute.put(CHAMPION_COLUMN, champion.ID)
        }
    }

    fun getChampion(index: Int): Champion{
        return champion
    }
    fun delete(context: Context, list: ArrayList<Task>){
        val fragmentEvenListener = context as FragmentEventListener
        if(pred.isEmpty() && depend.isEmpty()){
            //delete
            fragmentEvenListener.onDelete(this)
        }else if(pred.isEmpty()){
            //only has dependent
            //this is a first degree task
            Log.d("delete_task", "Pred is empty")
            for(dependent in depend){
                var temp = getById(dependent.ID, list)
                temp.removePred(this)
                fragmentEvenListener.onUpdate(dependent)
            }
            fragmentEvenListener.onDelete(this)
        }else if (depend.isEmpty()){
            //last task
            Log.d("delete_task", "Dependent is empty")
            for(predo in pred){
                predo.removeDependent(this)
                fragmentEvenListener.onUpdate(pred)
            }
            fragmentEvenListener.onDelete(this)
        }
        else{
            //loop through the dependents
            Log.d("delete_task", "Neither is empty")
            for(predo in pred){
                removeDependent(this)
                predo.setDepend(depend)
                fragmentEvenListener.onUpdate(predo)
            }
            for(dependent in depend){
                removePred(this)
                dependent.setPred(pred)
                fragmentEvenListener.onUpdate(dependent)
            }
            fragmentEvenListener.onDelete(this)
        }
    }

    private fun getById(id:Long, list: ArrayList<Task>): Task{
        var x:Task= Task()
        for(i in list){
            if(i.ID== id){
                x=i
            }
        }
        return x
    }
}