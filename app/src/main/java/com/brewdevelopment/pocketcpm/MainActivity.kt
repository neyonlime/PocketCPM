package com.brewdevelopment.pocketcpm



import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.ListView





class MainActivity : AppCompatActivity(), FragmentEventListener {


    //val DB1: DBAdapter= DBAdapter("Data",this)
    var isProj: Boolean= true
    private lateinit var menuList: Array<String>
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var drawerList:ListView
    private lateinit var fab: FloatingActionButton
    private lateinit var dbAdapter: DBAdapter
    private lateinit var  toolbar: Toolbar
    private lateinit var projectList: ArrayList<Project>

        lateinit var selectedProject: Project            //current displayed project
        lateinit var selectedTask: Task                 //current task




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

        dbAdapter = DBAdapter("data", this)
        projectList = ArrayList()


        //FAB
        fab = findViewById(R.id.floating_action_button) as FloatingActionButton
        fab.hide()
        fab.setOnClickListener{
            //define the on click action
            val fragment = fragmentManager.findFragmentById(R.id.content_frame)
            when(fragment.tag){
                DisplayFragment.PROJECT_KEY ->{
                    //add new project
                    var mFragment = AddProjectFragment.newAddInstance()
                    val fm = fragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.replace(R.id.content_frame, mFragment,AddProjectFragment.ADD_PROJECT)
                    fab.hide()
                    transaction.commit()
                }
                DisplayFragment.TASK_KEY ->{
                    var mFragment = AddTaskFragement()
                    val fm = fragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.replace(R.id.content_frame, mFragment, AddTaskFragement.ADD_TASK)
                    fab.hide()
                    transaction.commit()
                }
            }
        }


        drawerList.adapter= CustomAdapter(this) //set the adapter to custom one
        //this portion holds the events that occur on the click of a drawer list item//
        drawerList.onItemClickListener= AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id:Long ->

            if(position==0) {
              //home
                if(fab.isShown) fab.hide()
                drawerLayout.closeDrawers()

            }
            if(position==1) {
                fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.expand_arrow1600))
                fab.show()
                isProj=true
                val fragment = DisplayFragment.newInstance(dbAdapter.getProjects())
                val fm = fragmentManager
                val transaction = fm.beginTransaction()               
                transaction.replace(R.id.content_frame,fragment,DisplayFragment.PROJECT_KEY)
        
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            if(position==2) {

            }
        }

    }

    override fun onPause() {
        super.onPause()
        dbAdapter.close()
    }

    override fun onResume() {
        super.onResume()
        dbAdapter.open()
    }

    //Fragement communication interface
    override fun onProjectSelect(obj: Project) {
        selectedProject = obj
        val projName= obj.name
        toolbar.title=projName
        val fragment= DisplayFragment.newInstance(projName,dbAdapter.getTaskList(obj.ID))
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.content_frame,fragment)

        isProj=false // IMPORTANT: Whenever we introduce a back button that takes u back to projects, change the isProject
        transaction.commit()
    }

    override fun onTaskSelect(obj: Task) {
        selectedTask = obj
    }

    override fun onAdd(obj: Any) {
        when(obj){
            is Project -> {
                dbAdapter.save(obj)
                projectList.add(obj)
            }
            is Task -> {
                selectedProject.taskList.add(obj)
                dbAdapter.saveTask(selectedProject, obj)
            }
        }
    }

    override fun onEdit(obj: Any) {

    }

}

        //setting up the navigation

