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




    }
}
