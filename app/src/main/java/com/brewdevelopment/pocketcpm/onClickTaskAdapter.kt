package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
    lateinit var  startString: String
    lateinit var format:SimpleDateFormat
    lateinit var startDate: Date
    init{
        this.task=task
        this.project= project
        val CC=CritCalc(task,project)
        startString= project.start
        format = SimpleDateFormat("dd/MM/yyy")
        val cES = Calendar.getInstance()
        val cEF = Calendar.getInstance()
        val cLS = Calendar.getInstance()
        val cLF = Calendar.getInstance()
        try {
            cES.time = format.parse(startString)
            cLS.time = format.parse(startString)
            cLF.time = format.parse(startString)
            cEF.time = format.parse(startString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        cES.add(Calendar.DAY_OF_MONTH,CC.getEarlyStart(task))
        earlyS= format.format(cES.time)
        cEF.add(Calendar.DAY_OF_MONTH,CC.getEarlyFinish(task))
        earlyF= format.format(cEF.time)
        cLF.add(Calendar.DAY_OF_MONTH,CC.getLateFinish(task))
        lateF= format.format(cLF.time)
        cLS.add(Calendar.DAY_OF_MONTH,CC.getLateStart(task))
        lateS= format.format(cLS.time)
        slack=(CC.getLateFinish(task)-CC.getEarlyFinish(task)).toString()+" Days"
        if(CC.getLateFinish(task)-CC.getEarlyFinish(task)==0){
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
        val v: View? = LayoutInflater.from(parent?.context).inflate(R.layout.info_card, parent, false)
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
    lateinit var list: ArrayList<Task>
    init{
        this.project2= project2
        this.task=task
        list=project2.getTasks()
    }
    fun getById(id:Long): Task{
        var x:Task= Task()
        for(i in list){
            if(i.ID== id){
                x=i
            }
        }
        return x
    }
    fun getTOC(project: Project):Int{
        return project.getTOC(project)
    }
    fun getLateFinish(task:Task):Int{
            if (task.getDepend().isEmpty()) {// if no successors then the late finish is the time of completion of project
                LF = project2.getTOC(project2)
                Log.d("add_dependent", "${LF}")
            }

            else  {// if it does have successor then the late finish is the smallest late start of its successors
                    var min = Double.POSITIVE_INFINITY.toInt()
                    for (i in task.getDepend()) {
                        Log.d("add_dependent", i.attribute.get(Task.NAME_COLUMN).toString())
                        if (getLateStart(getById(i.ID)) < min) {
                            min = getLateStart(getById(i.ID))
                        }
                    }
                    LF = min
            }
        return LF
    }
    fun getEarlyFinish(task:Task):Int{
        EF= getEarlyStart(task)+task.attribute.get(Task.DURATION_COLUMN).toString().toInt() //early finish is early start plus duration
        return EF
    }
    fun getLateStart(task:Task):Int{
        LS= getLateFinish(task)-task.attribute.get(Task.DURATION_COLUMN).toString().toInt() //late start is late finish minus duration
        return LS
    }
    fun getEarlyStart(task: Task):Int{
            if (task.getPred().isEmpty()) { ///if it has no preds then Early start is 0
                ES = 0
            } else if (!task.getPred().isEmpty()) { // if it does have preds then get the max early finish of its preds and thats the early start of this one
                    var max: Int = 0
                    for (i in task.getPred()) {
                        if (getEarlyFinish(getById(i.ID)) > max) {
                            max = getEarlyFinish(getById(i.ID))
                            Log.e("getPred", max.toString())
                        }
                    }
                /*Log.e("PredTask of task "+task.attribute.get(Task.NAME_COLUMN), task.getPred()[0].attribute.get(Task.NAME_COLUMN).toString())
                Log.e("PredTask of task "+task.attribute.get(Task.NAME_COLUMN), task.getPred()[0].getPredList())
                Log.e("PredTask of task "+task.attribute.get(Task.NAME_COLUMN), getEarlyFinish(task.getPred()[0]).toString())*/
                    ES = max
            }
        return ES
    }
         /* fun isCrit():Boolean{
        var v: Boolean= false
        if(CritCalc(task,Project(":(")).getLateFinish()-CritCalc(task,project2).getEarlyFinish()==0){
            v=true
        }
        return v
    }*/

}
