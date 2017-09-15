package com.brewdevelopment.pocketcpm



import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
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
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setTitleMargin(0, 0, 0, 0)
        toolbar.setPaddingRelative(0,0,0,0)

        setSupportActionBar(toolbar)//setting the toolbar and providing functionality to the toolbar
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

                    //clearBackstack
                    fragmentManager.popBackStack()


                    transaction.commit()
                }

                TaskDisplayFragment.TASK_KEY ->{
                    var taskList = dbAdapter.getTaskList(selectedProject.ID)
                    var mFragment = AddTaskFragement.newInstance(taskList,dbAdapter.getChampionList(DBAdapter.ALL))
                    val fm = fragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.replace(R.id.content_frame, mFragment, AddTaskFragement.ADD_TASK)
                    fab.hide()
                    //clear backstack
                    fragmentManager.popBackStack()
                    transaction.commit()

                }

            }

        }

        drawerList.adapter= CustomAdapter(this) //set the adapter to custom one
        //this portion holds the events that occur on the click of a drawer list item//
        drawerList.onItemClickListener= AdapterView.OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id:Long ->

            if(position==0) {
              //Champ
                toolbar.title= "Champions"
                fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_plus))
                fab.hide()
                val fragment = Add_Champ_Frag.newInstance(dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
                transaction.replace(R.id.content_frame,fragment,Add_Champ_Frag.ADD_CHAMP)
                fragmentManager.popBackStack()
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            if(position==1) {
                //projects
                toolbar.title= "Projects"
                fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_plus))
                fab.show()
                isProj=true
                var projects: ArrayList<Project> = dbAdapter.getProjects()
                for(project in projects){
                    project.taskList = dbAdapter.getTaskList(project.ID)
                }
                val fragment = ProjectDisplayFragment.newInstance(projects)
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
                transaction.replace(R.id.content_frame,fragment,ProjectDisplayFragment.PROJECT_KEY)
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            if(position==2) {
                Toast.makeText(this, "'Diagrams' are not available for BETA!", Toast.LENGTH_LONG).show()
            }
        }

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }

        //setup the initial layout
        //should be a project list view
        //projects
        toolbar.title= "Projects"
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_plus))
        fab.show()
        isProj=true
        var projects: ArrayList<Project> = dbAdapter.getProjects()
        for(project in projects){
            project.taskList = dbAdapter.getTaskList(project.ID)
        }
        val fragment = ProjectDisplayFragment.newInstance(projects)
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
        transaction.replace(R.id.content_frame,fragment,ProjectDisplayFragment.PROJECT_KEY)
        transaction.commit()
    }

    override fun onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT)
        this.finish()
        Log.e("@@@", "onPause")
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT)
        var isOpen = true
        try {
            dbAdapter.checkDBState()

        }catch (e: Exception){
            //database is closed
            isOpen = false
        }
        if(isOpen){

            dbAdapter.close()
        }
        super.onPause()
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = fragmentManager.findFragmentById(R.id.content_frame)
        if(fragment !== null && fragment.tag == ProjectDisplayFragment.PROJECT_KEY || fragment.tag == TaskDisplayFragment.TASK_KEY) {
            fab.show()
            if(fragment.tag == ProjectDisplayFragment.PROJECT_KEY){
                toolbar.title = "Projects"
            }else if(fragment.tag == TaskDisplayFragment.TASK_KEY){
                toolbar.title = selectedProject.name
            }
        }
        if(fragment.tag == ProjectDisplayFragment.PROJECT_KEY){
            Log.e("###", "Project display")
            fragmentManager.popBackStack(ProjectDisplayFragment.PROJECT_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

    }

    override fun onResume() {
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT)
        super.onResume()
        dbAdapter.open()
    }

    override fun onDestroy() {
        Log.e("@@@", "onDestroy")
        dbAdapter.close()
        this.finish()
        super.onDestroy()
    }


    //Fragement communication interface
    override fun onProjectSelect(obj: Project) {
        //isProj=false // IMPORTANT: Whenever we introduce a back button that takes u back to projects, change the isProject
        selectedProject = obj
        val projName= obj.name
        toolbar.title=projName
        //Log.d("get_tasks", "MainActivity/${dbAdapter.getTaskList(selectedProject.ID).size}")

        val fragment= TaskDisplayFragment.newInstance(selectedProject.taskList)
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_back, R.anim.slide_out_back)
        transaction.replace(R.id.content_frame,fragment,TaskDisplayFragment.TASK_KEY)
        transaction.addToBackStack(TaskDisplayFragment.TASK_KEY)
        transaction.commit()
    }

    override fun onTaskSelect(obj: Task) {
        selectedTask = obj
        val taskName: String = obj.attribute.get(Task.NAME_COLUMN).toString()
        toolbar.title=taskName
        fab.hide()
        //Log.d("get_tasks", "MainActivity/${dbAdapter.getTaskList(selectedProject.ID).size}")
        val fragment= TaskViewFragment.newInstance(selectedTask, selectedProject)
        val fm = fragmentManager
        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_back, R.anim.slide_out_back)
        transaction.replace(R.id.content_frame,fragment, TaskViewFragment.TASK)
        transaction.addToBackStack(TaskViewFragment.TASK)
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
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
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
                val fragment= TaskDisplayFragment.newInstance(dbAdapter.getTaskList(selectedProject.ID))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame,fragment,TaskDisplayFragment.TASK_KEY)
                transaction.commit()
            }

            is Project -> {
                dbAdapter.delete(obj)
                val fragment = ProjectDisplayFragment.newInstance(dbAdapter.getProjects())
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, fragment, ProjectDisplayFragment.PROJECT_KEY)
                transaction.commit()
            }

            is Champion -> {
                dbAdapter.delete(obj)
                val mFragment = Add_Champ_Frag.newInstance(dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, mFragment, Add_Champ_Frag.EDIT_CHAMP)
                transaction.commit()
            }

        }
    }

    override fun onEdit(obj: Any) {
        when(obj){
            is Project -> {
                val mFragment = AddProjectFragment.newEditInstance(obj)
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, mFragment, AddProjectFragment.EDIT_PROJECT)
                fab.hide()
                transaction.commit()
            }
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

            is Champion -> {
                val mFragment = Add_Champ_Frag.newInstance(obj,dbAdapter.getChampionList(DBAdapter.ALL))
                val fm = fragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.content_frame, mFragment, Add_Champ_Frag.EDIT_CHAMP)
                transaction.commit()
            }
        }
    }

    override fun onRefreshChampion() {
        val fragment = Add_Champ_Frag.newInstance(dbAdapter.getChampionList(DBAdapter.ALL))
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment ,Add_Champ_Frag.ADD_CHAMP).commit()
    }

}

        //setting up the navigation

