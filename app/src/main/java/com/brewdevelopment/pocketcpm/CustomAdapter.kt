package com.brewdevelopment.pocketcpm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

open class CustomAdapter(context: Context, resource: Array<my_items>): ArrayAdapter<my_items>(context, R.layout.nav_row ,resource) {
     var list: ArrayList<my_items> = ArrayList<my_items>()
    val menuList = arrayOf("Tasks","Projects","Diagrams")
    val menuList2: Array<Int> = arrayOf(R.drawable.download,R.drawable.sasukepart1,R.drawable.download)


   for(i in 0..2)
    {

    }

    override fun getView(Position: Int, CV: View?, Parent: ViewGroup): View?{
      val inf : LayoutInflater = LayoutInflater.from(context)
     val CustomView: View = inf.inflate(R.layout.nav_row, Parent,false)
        val specTxt= getItem(Position)

        val Txt: TextView= CustomView.findViewById(R.id.NavTV) as TextView
        val Img: ImageView = CustomView.findViewById(R.id.NavIM) as ImageView
        Txt.text= specTxt 
        Img.setImageResource(R.drawable.sasukepart1)
        return CustomView


    }

}
