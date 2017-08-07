package com.brewdevelopment.pocketcpm

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView



class MainActivity : AppCompatActivity() {

    private lateinit var menuList: Array<String>
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var drawerList:ListView



    override fun onCreate(savedInstanceState: Bundle?) {  //question marks denote nullable types
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_view)



        //setting up the toolbar
        val toolbar: Toolbar
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)        //setting the toolbar and providing functionality to the toolbar

        menuList = arrayOf("Tasks","Projects","Diagrams")
       //val menuList2: Array<Int> = arrayOf(R.drawable.download,R.drawable.sasukepart1,R.drawable.download)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout     //casting is done using the as keyword

        drawerList = findViewById(R.id.left_drawer) as ListView

        drawerList.adapter= CustomAdapter(this) //set the adapter to custom one
        //this portion holds the events that occur on the click of a drawer list item
        drawerList.onItemClickListener= AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id:Long ->
            if(position==0) {
                drawerLayout.closeDrawers()
            }
            if(position==1) {
                drawerLayout.closeDrawers()
            }
            if(position==2) {
                drawerLayout.closeDrawers()
            }
        }





        }



        }




        //setting up the navigation




