package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView

/**
 * Created by Osama on 2017-08-22.
 */

class ProjAdapter ( c: Context) : RecyclerView.Adapter<ProjAdapter.ViewHolder>() {


    val DB1 = DBAdapter("DataCA", c)

    var list = ArrayList<Project>()

    init {
        list = (DB1.getProjects())
    }
    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView){
        val currentItem: Int=0
        val item_Title= itemView?.findViewById(R.id.Title) as TextView
        val item_Desc= itemView?.findViewById(R.id.Desc) as TextView
    }


    override fun onCreateViewHolder(parent: ViewGroup?, i: Int): ViewHolder {
        var v:View?=null
        if(list.size<1){
             v=null
        }
        else {
             v = LayoutInflater.from(parent?.context).inflate(R.layout.card_layout, parent, false)
        }
        val viewHolder: ViewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val temp: Project = list[position]
        holder?.item_Title?.text = temp.name
        holder?.item_Desc?.text= temp.getTotalTime().toString()

    }


    override fun getItemCount(): Int {
        return list.size
    }
}

class RecyclerItemClickListener(context: Context, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {
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

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}


