package com.brewdevelopment.pocketcpm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView


/**
 * Created by Osama on 2017-08-16.
 */
class RecyclerAdapter( var context: Context,var list: ArrayList<Task>) : RecyclerView.Adapter<RecyclerAdapter.viewHolder>()
{

    class viewHolder(itemView: View?): RecyclerView.ViewHolder(itemView){
        val currentItem: Int=0
        val item_Title= itemView?.findViewById(R.id.Title) as TextView
        val item_Desc= itemView?.findViewById(R.id.Desc) as TextView
        val options = itemView?.findViewById(R.id.options_button) as ImageView
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


        var fragmentEventlistener = context as FragmentEventListener

        //onClick listener for the options menu
        holder?.options!!.setOnClickListener{
            Log.d("edit_task", "edit task Clicked")
            val menu: PopupMenu = PopupMenu(context, holder.options)
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