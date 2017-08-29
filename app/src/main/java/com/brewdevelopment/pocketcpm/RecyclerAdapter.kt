package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


/**
 * Created by Osama on 2017-08-16.
 */
class RecyclerAdapter( context: Context,var list: ArrayList<Task>) : RecyclerView.Adapter<RecyclerAdapter.viewHolder>()
{

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
        val temp: Task= list[position]
        holder?.item_Title?.text= temp.attribute.get(Task.NAME_COLUMN) as String
        holder?.item_Desc?.text= temp.attribute.get(Task.DESCRIPTION_COLUMN) as String

    }

    override fun getItemCount(): Int {
        return list.size

    }

}