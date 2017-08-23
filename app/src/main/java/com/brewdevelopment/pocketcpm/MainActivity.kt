package com.brewdevelopment.pocketcpm


import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView




class MainActivity : AppCompatActivity(),  FragmentEventListener {

    override fun onProjectSelect(obj: Project) {

        val projname= obj.name
        toolbar.title=projname
        DisplayFragment().recyclerView?.adapter= RecyclerAdapter(this,obj)
        val fragment= DisplayFragment.newInstance(projname,DB1.getTaskList(obj.ID))
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.content_frame,fragment)
        transaction.commit()
        isProj=false // IMPORTANT: Whenever we introduce a back button that takes u back to projects, change the isProject back to true

    }

    override fun onTaskSelect(obj: Task) {

    }

    override fun onAdd(obj: Any) {

    }

    override fun onEdit(obj: Any) {

    }

    val DB1: DBAdapter= DBAdapter("Data",this)
    var isProj: Boolean= true
    private lateinit var menuList: Array<String>
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var drawerList:ListView
    private lateinit var  toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {  //question marks denote nullable types
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)
        //setting up the toolbar

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)        //setting the toolbar and providing functionality to the toolbar
        menuList = arrayOf("Dashboard","Projects","Diagrams")
       //val menuList2: Array<Int> = arrayOf(R.drawable.download,R.drawable.sasukepart1,R.drawable.download)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout     //casting is done using the as keyword
        drawerList = findViewById(R.id.left_drawer) as ListView
        drawerList.adapter= CustomAdapter(this) //set the adapter to custom one
        //this portion holds the events that occur on the click of a drawer list item//
        drawerList.onItemClickListener= AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id:Long ->

            if(position==0) {

            }
            if(position==1) {
                isProj=true
                val fragment = DisplayFragment.newInstance(DB1.getProjects())
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame,fragment)
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            if(position==2) {

            }
        }

    }
}

        //setting up the navigation

