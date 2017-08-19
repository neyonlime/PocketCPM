package com.brewdevelopment.pocketcpm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

 class CustomAdapter(c: Context): BaseAdapter() {
    var list = ArrayList<my_items>()
    val menuList = arrayOf("Tasks","Projects","Diagrams")
    val menuList2: Array<Int> = arrayOf(R.drawable.download,R.drawable.sasukepart1,R.drawable.download)
    val context = c

     //this init function holds a ghetto for loop AKA while loop to fill the array list with the objects
    init{
        var x: Int =0
        while(x<=2){
            list.add(my_items(menuList[x],menuList2[x]))
            x++
        }
    }

    //The next few functions are overrides to change the count/ how the adapter picks the items
    override fun getCount(): Int {
       return list.size
   }

    override fun getItem(position: Int): my_items {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // this is the main override where babys are created
    override fun getView(position: Int, CV: View?, Parent: ViewGroup): View?{
        //here we get a layout inflater and make it inflate the single row resource file
        val inf : LayoutInflater = LayoutInflater.from(context)
        val CustomView: View? = inf.inflate(R.layout.nav_row, Parent,false)
        //
        //here we reffrence the text and image views that are inside the CustomView
        val Txt: TextView= CustomView?.findViewById(R.id.NavTV) as TextView
        val Img: ImageView = CustomView.findViewById(R.id.NavIM) as ImageView
        //
        //here we make a temp my_items instance that has the items included in that "position"
        val temp: my_items= list[position]
        //
        //set the text and image of the row to whatever is in the list at that exact position
        Txt.text= temp.txt
        Img.setImageResource(temp.img)
        //
        //return the view we just created
        return CustomView

    }

}
