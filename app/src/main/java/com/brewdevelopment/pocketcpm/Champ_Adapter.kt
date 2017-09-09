package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by neyon on 2017-09-06.
 */
class Champ_Adapter(context: Context, var list: ArrayList<Champion>) : RecyclerView.Adapter<Champ_Adapter.ViewHolder>() {

    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView){
        //val currentItem: Int=0
        val item_Title= itemView?.findViewById(R.id.Title) as TextView
        val item_Desc= itemView?.findViewById(R.id.Desc) as TextView
    }

    override  fun onCreateViewHolder(parent: ViewGroup?, i: Int): ViewHolder {
        val v: View? = LayoutInflater.from(parent?.context).inflate(R.layout.card_layout, parent, false)
        val viewHolder: ViewHolder= ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val temp: Champion = list[position]
        holder?.item_Title?.text= temp.name
        holder?.item_Desc?.visibility= View.GONE

    }

    override fun getItemCount(): Int {
        return list.size
    }

}