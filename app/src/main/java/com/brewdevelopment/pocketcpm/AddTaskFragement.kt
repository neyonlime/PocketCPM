package com.brewdevelopment.pocketcpm


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.Serializable

import java.util.*
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter

/**
 * Created by neyon on 2017-07-29.
 * this fragment handels adding new tasks
 * gets passed the Activity of the task that called the fragment if it is being called for an edit
 */


class AddTaskFragement : Fragment(), AdapterView.OnItemSelectedListener  {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView2: RecyclerView
    lateinit var task1: Task
    lateinit var Pred: Task
    private lateinit var task: Task


    lateinit private var taskButton: Button
    lateinit private var selectedPredTask: Task             //predecessor task
    lateinit var fragmentEventListener: FragmentEventListener
    private var editTask: Task? = null           // the task that is going to be edited
    private lateinit var selectedChampion: Champion
    private lateinit var championList: ArrayList<Champion>


    constructor(task: Task):this(){
        this.editTask = task
    }

    companion object {
        val ADD_TASK = "add_task"
        val ALL_LIST = "all_tasks"
        val ALL_CHAMPIONS = "all_champions"
        val EMPTY: Long = -1
        val EDIT_TASK = "edit_task"
        fun newInstance(allTasks: ArrayList<Task>, allChampions: ArrayList<Champion>): AddTaskFragement{
            //creating a brand NEW task
            Log.d("add_task", "${allTasks.size}")
            var args: Bundle = Bundle()
            args.putSerializable(ALL_LIST, allTasks as Serializable)
            args.putSerializable(ALL_CHAMPIONS, allChampions as Serializable)
            var fragment: AddTaskFragement = AddTaskFragement()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
        }

        fun newInstance(callingTask: Task, allTasks: ArrayList<Task>, allChampions: ArrayList<Champion>): AddTaskFragement{
            //Being called from a task for an EDIT
            //create and pass the bundle containing the attributes of the task
            Log.d("edit_task", "AddtaskFragmet/newInstance()/taskList: ${allTasks.size}")
            var args = Bundle()
            args.putSerializable(ALL_LIST, allTasks as Serializable)
            args.putSerializable(ALL_CHAMPIONS, allChampions as Serializable)
            var fragment = AddTaskFragement(callingTask)
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var predList = ArrayList<Task>()
        if(editTask===null) {
             predList = ArrayList<Task>()
        }
        else{
            predList= fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(EDIT_TASK) as ArrayList <Task> //CHANGE
        }

        var rootView = inflater?.inflate(R.layout.fragement_add_task,container, false)
        val taskName = rootView!!.findViewById(R.id.task_name_field) as EditText
        val championDropdown = rootView!!.findViewById(R.id.champion_dropdown) as Spinner
        val description = rootView.findViewById(R.id.description_field) as EditText
        val duration = rootView.findViewById(R.id.duration_field) as EditText

        recyclerView= rootView?.findViewById(R.id.recycler_view1) as RecyclerView
        recyclerView2=rootView?.findViewById(R.id.recycler_view2) as RecyclerView
        var list = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_LIST) as ArrayList <Task>
        var mAdapter= TaskAdapter(activity,list)
        var mAdapter2= PredAdapter(activity,predList)
      
        recyclerView2.adapter = PredAdapter(activity, predList)
        recyclerView2.layoutManager = LinearLayoutManager(activity)
        recyclerView2.invalidate()
        recyclerView.adapter = TaskAdapter(activity, list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.invalidate()


        selectedPredTask = Task() 


        recyclerView2.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        Pred = PredAdapter(activity, predList).list[position]
                        list.add(Pred)
                        predList.remove(Pred)
                        mAdapter2.notifyDataSetChanged()
                        recyclerView.swapAdapter(mAdapter, false)
                        recyclerView2.swapAdapter(mAdapter2, false)
                        mAdapter.notifyDataSetChanged()
                        mAdapter2.notifyItemRemoved(position)
                        mAdapter2.notifyItemRangeChanged(position, predList.size)
                        recyclerView.invalidate()
                        Log.e("@@@@@", "" + position)
                    }
                })
        )
        //her*esas
        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        task1 = TaskAdapter(activity, list).list[position]
                        predList.add(task1)
                        list.remove(task1)
                        recyclerView.swapAdapter(mAdapter, false)
                        recyclerView2.swapAdapter(mAdapter2, false)
                        mAdapter.notifyItemRemoved(position)
                        mAdapter.notifyItemRangeChanged(position, list.size)
                        recyclerView2.invalidate()
                        Log.e("@@@@@", "" + position)
                    }
                })
        )
                                                         //the task that is being added to the predecessor

        //get the tasks from bundle
        var tasks: ArrayList<Task> = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_LIST) as ArrayList<Task>

        selectedChampion = Champion()
        taskButton = rootView!!.findViewById(R.id.task_button) as Button


        championList = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_CHAMPIONS) as ArrayList<Champion>
        //make a string of all the names
        var championNames = ArrayList<String>()
        for(champ in championList){
            championNames.add(champ.name)
        }
        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, championNames)
        championDropdown.adapter = adapter
        championDropdown.onItemSelectedListener = this

        //check if editing or adding
        if(editTask !== null){
            //open for editing
            //load the information into to views
            Log.d("edit_task", "AddTaskFragment/onCreatView()/editing task: ${editTask!!.ID}")

            taskName.setText("${editTask!!.attribute.get(Task.NAME_COLUMN)}")
            championDropdown.prompt = "Champion"
            var position = getChampionPosition(editTask!!.getChampion(0))
            if(position != -1){championDropdown.setSelection(position)}
            duration.setText("${editTask!!.attribute.get(Task.DURATION_COLUMN)}")
            description.setText("${editTask!!.attribute.get(Task.DESCRIPTION_COLUMN)}")
            taskButton.setText("Save Changes")

            //load the predecessors into the recycler views

        }else if(editTask === null){
            //open for adding

        }

        //get the tasks from bundle
        var tasks: ArrayList<Task> = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_LIST) as ArrayList<Task>

        taskButton.setOnClickListener {
            //the save button has been clicked, store or append the data
            //build the task object

            if(editTask !== null){
                //edit
                editTask!!.attribute.put(Task.NAME_COLUMN, taskName.text.toString())
                editTask!!.setPred(predList)
                editTask!!.attribute.put(Task.DURATION_COLUMN, "${duration.text}")
                editTask!!.attribute.put(Task.DESCRIPTION_COLUMN, description.text.toString())

                if(validateTask(editTask!!)){
                    fragmentEventListener.onUpdate(editTask!!)

                    var champion: Champion = selectedChampion            //getting current champion
                    //update the champion
                    if(champion.ID !=  EMPTY){
                        editTask!!.setChampion(champion)
                    }

                    if(validateChampion(champion)){
                        fragmentEventListener.onUpdate(champion)
                    }
                }
            }else if(editTask === null){
                var task = Task()
                task.attribute.put(Task.NAME_COLUMN, taskName.text.toString())
                task.setPred(predList)
                task.attribute.put(Task.DURATION_COLUMN, "${duration.text}")
                task.attribute.put(Task.DESCRIPTION_COLUMN, description.text.toString())
                var champion = selectedChampion
                if(champion.ID != EMPTY ){
                    task.setChampion(champion)
                }

                if(validateTask(task)) {
                    //all information about the task is valid
                    //then save the task to database
                    fragmentEventListener.onAdd(task)
                }
            }
            list.add(task)
        }
        return rootView
    }
    
    fun getChampionPosition(champion: Champion): Int{
        for(i in 0..championList.size){
            if(champion.ID == championList.get(i).ID){
                return i
            }
        }
        return -1
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //set the selected champion to the champion object selected by the user
        selectedChampion = championList.get(position)
    }

    private fun validateChampion(champion: Champion): Boolean {
        //validate the champion object
        return true
    }

    private fun validateTask(task: Task): Boolean{
        //validate the task before saving
        return true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context         // casts the calling activity to the implementation on AddFragmentEventsListener

    }
}
