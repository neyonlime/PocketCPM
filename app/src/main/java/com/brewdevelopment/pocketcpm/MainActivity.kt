package com.brewdevelopment.pocketcpm



import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast


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
        selectedProject= Project()
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

                ProjectDisplayFragment.PROJECT_KEY ->{
                    //add new project
                    var mFragment = AddProjectFragment.newAddInstance()
                    val fm = fragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.replace(R.id.content_frame, mFragment,AddProjectFragment.ADD_PROJECT)
                    fab.hide()
                    transaction.commit()
                }

                TaskDisplayFragment.TASK_KEY ->{
                    var taskList = dbAdapter.getTaskList(selectedProject.ID)
                    var mFragment = AddTaskFragement.newInstance(taskList,dbAdapter.getChampionList(DBAdapter.ALL))
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
              //Champ
                fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_plus))
                fab.hide()
                val fragment = Add_Champ_Frag.newInstance(dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame,fragment,Add_Champ_Frag.ADD_CHAMP)
                transaction.commit()
                drawerLayout.closeDrawers()


            }
            if(position==1) {
                fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_plus))
                fab.show()
                isProj=true
                val fragment = ProjectDisplayFragment.newInstance(dbAdapter.getProjects())
                val fm = fragmentManager
                val transaction = fm.beginTransaction()               
                transaction.replace(R.id.content_frame,fragment,ProjectDisplayFragment.PROJECT_KEY)
        
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            if(position==2) {

            }
        }

    }

    override fun onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT)
        super.onPause()
        dbAdapter.close()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT)
        super.onResume()
        dbAdapter.open()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbAdapter.close()
    }

    //Fragement communication interface
    override fun onProjectSelect(obj: Project) {
        //isProj=false // IMPORTANT: Whenever we introduce a back button that takes u back to projects, change the isProject
        selectedProject = obj
        val projName= obj.name
        toolbar.title=projName
        selectedProject.taskList = dbAdapter.getTaskList(selectedProject.ID)

        //Log.d("get_tasks", "MainActivity/${dbAdapter.getTaskList(selectedProject.ID).size}")

        val fragment= TaskDisplayFragment.newInstance(selectedProject.taskList)
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.content_frame,fragment,TaskDisplayFragment.TASK_KEY)
        transaction.commit()
    }

    override fun onTaskSelect(obj: Task) {
        selectedTask = obj
        val taskName: String = obj.attribute.get(Task.NAME_COLUMN).toString()
        toolbar.title=taskName
        //Log.d("get_tasks", "MainActivity/${dbAdapter.getTaskList(selectedProject.ID).size}")
        val fragment= TaskViewFragment.newInstance(selectedTask, selectedProject)
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.content_frame,fragment)
        transaction.commit()
    }

    override fun onAdd(obj: Any) {
        when(obj){
            is Project -> {
                dbAdapter.save(obj)
                projectList.add(obj)
                selectedProject = obj

                Toast.makeText(this,"Project Saved", Toast.LENGTH_SHORT)
                val fragment = AddTaskFragement.newInstance(dbAdapter.getTaskList(selectedProject.ID), dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, fragment, AddTaskFragement.ADD_TASK)
                transaction.commit()
            }
            is Task -> {
                Log.d("add_task", "MainActivity/Project(${selectedProject.ID}): ${selectedProject.taskList.size}")
                selectedProject.taskList.add(obj)
                Log.d("add_task", "MainActivity/Project After(${selectedProject.ID}): ${selectedProject.taskList.size}")
                dbAdapter.saveTask(selectedProject, obj)
            }
            is Champion ->{
                dbAdapter.save(obj)
            }
        }
    }

    override fun onUpdate(obj: Any) {
        when(obj){
            is Project ->{
                dbAdapter.save(obj)
            }
            is Task -> {
                Log.d("add_task", "calling to update task: ${obj.ID}")
                dbAdapter.saveTask(selectedProject, obj)
            }
            is Champion -> {
                dbAdapter.save(obj)
            }
        }
    }
    override fun onDelete(obj: Any) {
        when(obj){
            is Task -> {
                //delete a task from the database
                dbAdapter.delete(obj)
            }
        }
    }
    override fun onEdit(obj: Any) {
        when(obj){
            is Project -> {}
            is Task -> {
                //open the fragement to edit the task
                var taskList = dbAdapter.getTaskList(selectedProject.ID)

                //fill the task's champion list
                //for if we ever want to add multiple champions for one task
                val championID= obj.attribute.get(Task.CHAMPION_COLUMN)
                if(championID !== null){
                    val champion = dbAdapter.getChampionByID(championID.toString())
                    if (champion !== null){
                        obj.setChampion(champion)
                    }
                }


                var mFragment = AddTaskFragement.newInstance(obj, taskList, dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, mFragment, AddTaskFragement.EDIT_TASK)
                fab.hide()
                transaction.commit()
            }
        }
    }

}

        //setting up the navigation

