package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Osama on 2017-08-31.
 */
class onClickTaskAdapter(context: Context, task: Task, project: Project) : RecyclerView.Adapter<onClickTaskAdapter.viewHolder>() {
    var list= ArrayList<mCrit>()
    val menuList = arrayOf("Early Start","Early Finish","Late Start", "Late Finish", "Slack Time", "Critical?")
    val menuList2 = ArrayList<String>()
    lateinit var task: Task
    lateinit var project: Project

    lateinit var earlyS: String
    lateinit var earlyF: String
    lateinit var lateS: String
    lateinit var lateF: String
    lateinit var slack: String
    lateinit var crit: String
    var ES: Int= 0
    var LS: Int= 0
    var LF: Int= 0
    var EF: Int= 0
    var SK : Int= 0
    var dur: Int =0
    var max: Int =0
    init{
        this.task=task
        this.project= project

        earlyS= CritCalc(task,project).getEarlyStart().toString()
        earlyF= CritCalc(task,project).getEarlyFinish().toString()
        lateF= CritCalc(task,project).getLateFinish().toString()
        lateS= CritCalc(task,project).getLateStart().toString()
        slack=(CritCalc(task,project).getLateFinish()-CritCalc(task,project).getEarlyFinish()).toString()
        if(CritCalc(task,project).getLateFinish()-CritCalc(task,project).getEarlyFinish()==0){
            crit= "Yes"
        }
        else{
            crit= "No"
        }
        menuList2.add(earlyS)
        menuList2.add(earlyF)
        menuList2.add(lateS)
        menuList2.add(lateF)
        menuList2.add(slack)
        menuList2.add(crit)

        for(i in 0..5){
            list.add(mCrit(menuList[i],menuList2[i]))
        }
    }
    class viewHolder(itemView: View?): RecyclerView.ViewHolder(itemView){
        //val currentItem: Int=0
        val item_Title= itemView?.findViewById(R.id.Title) as TextView
        val item_Desc= itemView?.findViewById(R.id.Desc) as TextView

    }

    override  fun onCreateViewHolder(parent: ViewGroup?, i: Int): viewHolder {
        val v: View? = LayoutInflater.from(parent?.context).inflate(R.layout.card_layout, parent, false)
        val viewHolder: viewHolder= viewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: viewHolder?, position: Int) {
        val temp: mCrit= list[position]
        holder?.item_Title?.text= temp.topic
        holder?.item_Desc?.text= temp.Val

    }

    override fun getItemCount(): Int {
        return list.size

    }
}
class mCrit(topic:String?, Val:String?) {

    var topic: String?
    var  Val: String?

    init{
        this.topic=topic
        this.Val=Val
    }
}
class CritCalc(task: Task, project2: Project){
    var ES: Int=0
    var LS: Int=0
    var LF: Int=0
    var EF: Int=0
    var task: Task
    var project2: Project
    var max: Int =0
    var min: Int=0
    init{
        this.project2= project2
        this.task=task

        //if(task.getPred().size==0) { ///if it has no preds then Early start is 0
       //     ES  = 0
       // }
         if(task.getPred().isEmpty()){ // if it does have preds then get the max early finish of its preds and thats the early start of this one
             Log.e("getPred", "entered")

            for (i in task.getPred()){
                if(CritCalc(i,project2).getEarlyFinish()>max) {
                    max = CritCalc(i,project2).getEarlyFinish()
                    Log.e("getPred", max.toString())
                }
            }

            ES=max
        }
///////////////////////////////////////////////////////////////////////////////////
        if(task.getDepend().size==0) {// if no successors then the late finish is the time of completion of project
            LF= project2.getTOC()
        }
        else if(task.getDepend().size!=0){// if it does have successor then the late finish is the smallest late start of its successors
            min=Double.POSITIVE_INFINITY.toInt()
            for (i in 0..task.getDepend().size){
                if(CritCalc(task.getDepend()[i],project2).getLateStart()<min) {
                    min = CritCalc(task.getDepend()[i],project2).getLateStart()
                }
            }
            LF=min
        }
        LS= LF-task.attribute.get(Task.DURATION_COLUMN).toString().toInt() //late start is late finish minus duration
        EF= ES+task.attribute.get(Task.DURATION_COLUMN).toString().toInt() //early finish is early start plus duration
    }
    fun getLateFinish():Int{
        return LF
    }
    fun getEarlyFinish():Int{
        return EF
    }
    fun getLateStart():Int{
        return LS
    }
    fun getEarlyStart():Int{
        return ES
    }
    fun isCrit():Boolean{
        var v: Boolean= false
        if(CritCalc(task,project2).getLateFinish()-CritCalc(task,project2).getEarlyFinish()==0){
            v=true
        }
        return v
    }

}