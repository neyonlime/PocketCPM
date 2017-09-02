package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
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
    lateinit var arr: Array<String>
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
        if(CritCalc(task,project).getLateFinish()-CritCalc(task,project).getEarlyFinish()==0){
            slack= "Yes"
        }
        else{
            slack= "No"
        }
        menuList2.add(earlyS)
        menuList2.add(earlyF)
        menuList2.add(lateS)
        menuList2.add(lateF)
        menuList2.add(slack)
        menuList2.add(crit)
        arr= menuList2.toArray(arr)
        for(i in 0..5){
            list.add(mCrit(menuList[i],arr[i]))
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
class CritCalc(task: Task, project: Project){
    var ES: Int=0
    var LS: Int=0
    var LF: Int=0
    var EF: Int=0
    var task: Task
    var project: Project
    var max: Int =0
    var min: Int=0
    init{
        this.project= project
        this.task=task
        if(task.getPred().size==0) {
            ES  = 0
        }
        else if(task.getPred().size!=0){
            max=CritCalc(task.getPred()[0],project).getLateFinish()
            for (i in 0..task.getPred().size){
                if(CritCalc(task.getPred()[i],project).getLateFinish()>max) {
                    max = CritCalc(task.getPred()[i],project).getLateFinish()
                }
            }
            ES=max
        }
///////////////////////////////////////////////////////////////////////////////////
        if(task.getDepend().size==0) {
            LF= project.getTOC()
        }
        else if(task.getPred().size!=0){
            min=CritCalc(task.getDepend()[0],project).getLateStart()
            for (i in 0..task.getDepend().size){
                if(CritCalc(task.getDepend()[i],project).getLateStart()<min) {
                    min = CritCalc(task.getPred()[i],project).getLateFinish()
                }
            }
            LF=min
        }
        LS= LF-task.attribute.get(Task.DURATION_COLUMN).toString().toInt()
        EF= ES+task.attribute.get(Task.DURATION_COLUMN).toString().toInt()
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
}