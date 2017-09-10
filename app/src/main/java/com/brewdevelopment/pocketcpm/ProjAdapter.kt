package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView

/**
 * Created by Osama on 2017-08-22.
 */

class ProjAdapter ( val context: Context, var list: ArrayList<Project>) : RecyclerView.Adapter<ProjAdapter.ViewHolder>() {


    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView){
        val currentItem: Int=0
        val item_Title= itemView?.findViewById(R.id.Title) as TextView
        val item_Desc= itemView?.findViewById(R.id.Desc) as TextView
        val options = itemView?.findViewById(R.id.options_button) as ImageView
    }


    override fun onCreateViewHolder(parent: ViewGroup?, i: Int): ViewHolder {
        var v:View?=null
        if(list.size < 1){
             v=null
        }
        else {
             v = LayoutInflater.from(parent?.context).inflate(R.layout.proj_card, parent, false)
        }
        val viewHolder: ViewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val temp: Project = list[position]
        holder?.item_Title?.text = temp.name
        holder?.item_Desc?.text= "Duration of "+CritCalc(Task(), temp).getTOC(temp).toString()+ " Days"


        holder?.options!!.setOnClickListener{
            val fragmentEventlistener = context as FragmentEventListener
            val menu = PopupMenu(context, holder.options)
            menu.menuInflater.inflate(R.menu.task_options, menu.menu)
            menu.show()

            menu.setOnMenuItemClickListener{
                when(it.itemId) {
                    R.id.task_edit -> {
                        //edit the selected task
                        fragmentEventlistener.onEdit(temp)
                    }
                    R.id.task_delete -> {
                        //delete the selected task
                        temp.delete(context)
                    }
                    else -> {}
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class RecyclerItemClickListener(context: Context, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    internal var mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }
        return false
    }


}


