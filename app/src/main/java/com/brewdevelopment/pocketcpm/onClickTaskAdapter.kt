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
class onClickTaskAdapter(context: Context) : RecyclerView.Adapter<onClickTaskAdapter.viewHolder>() {
    var list= ArrayList<mCrit>()
    val menuList = arrayOf("Early Start","Early Finish","Late Start", "Late Finish", "Slack Time", "Critical?")
    val menuList2 = ArrayList<String>()
    val task: Task = AddTaskFragement().getTask()
    lateinit var arr: Array<String>
    lateinit var earlyS: String
    lateinit var earlyF: String
    lateinit var lateS: String
    lateinit var lateF: String
    lateinit var slack: String
    lateinit var crit: String
    init{
        var dur= task.getDuration()

        val ES: Int =0
        val EF= ES+dur
        val LS: Int=0
        val LF= LS+ dur
        val SK= LF-EF
        if(SK.equals(0)){
            crit= "Yes"
        }
        else if(SK>0){
            crit= "No"
        }
        earlyS= ES.toString()
        earlyF= EF.toString()
        lateF= LF.toString()
        lateS= LS.toString()
        slack= SK.toString()
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