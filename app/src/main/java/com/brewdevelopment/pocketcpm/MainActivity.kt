package com.brewdevelopment.pocketcpm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit var menuList: Array<String>
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var drawerList:ListView



    override fun onCreate(savedInstanceState: Bundle?) {  //question marks denote nullable types
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)

        menuList = arrayOf("Tasks","Projects","Diagrams")
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout     //casting is done using the as keyword
        drawerList = findViewById(R.id.left_drawer) as ListView

        //setting up the navigation


    }
}
