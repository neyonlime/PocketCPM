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

/**
 * Created by neyon on 2017-07-29.
 * this fragment handels adding new tasks
 * gets passed the Activity of the task that called the fragment if it is being called for an edit
 */

class AddTaskFragement : Fragment(), AdapterView.OnItemClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView2: RecyclerView
    lateinit var task1: Task
    lateinit var Pred: Task
    private lateinit var task: Task

    lateinit private var taskButton: Button
    lateinit private var selectedPredTask: Task             //predecessor task
    lateinit var fragmentEventListener: FragmentEventListener

    companion object {
        val ADD_TASK = "add_task"
        val ALL_LIST = "all_tasks"
        val EDIT_TASK = "edit_task"
        fun newInstance(allTasks: ArrayList<Task>): AddTaskFragement{
            //creating a brand NEW task
            Log.d("add_task", "${allTasks.size}")
            var args: Bundle = Bundle()
            args.putSerializable(ALL_LIST, allTasks as Serializable)
            var fragment: AddTaskFragement = AddTaskFragement()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
        }

        fun newInstance(callingTask: Task): AddTaskFragement{
            //Being called from a task for an EDIT
            //create and pass the bundle containing the attributes of the task
            var fragment = AddTaskFragement()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var predList = ArrayList<Task>()
        if(editTask==null) {
             predList = ArrayList<Task>()
        }
        else{
            predList= fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(EDIT_TASK) as ArrayList <Task>
        }

        var rootView = inflater?.inflate(R.layout.fragement_add_task,container, false)
        val taskName = rootView!!.findViewById(R.id.task_name_field) as EditText
        val championName = rootView!!.findViewById(R.id.champion_name_field) as EditText
        val predecessorSpinner = rootView!!.findViewById(R.id.predecessor_spinner) as Spinner
        val description = rootView.findViewById(R.id.description_field) as EditText
        val duration = rootView.findViewById(R.id.duration_field) as EditText
        recyclerView= rootView?.findViewById(R.id.recycler_view1) as RecyclerView
        recyclerView2=rootView?.findViewById(R.id.recycler_view2) as RecyclerView
        var list = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_LIST) as ArrayList <Task>
        var mAdapter= TaskAdapter(activity,list)
        var mAdapter2= PredAdapter(activity,predList)


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
        recyclerView2.adapter = PredAdapter(activity, predList)
        recyclerView2.layoutManager = LinearLayoutManager(activity)
        recyclerView2.invalidate()


        recyclerView.adapter = TaskAdapter(activity, list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.invalidate()


        selectedPredTask = Task()                                                   //the task that is being added to the predecessor

        //get the tasks from bundle
        var tasks: ArrayList<Task> = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(ALL_LIST) as ArrayList<Task>

        //build string array of all task names
        var taskNames = ArrayList<String>()
        for(task in tasks){
            taskNames.add(task.attribute.get(Task.NAME_COLUMN).toString())
        }

        Log.d("add_task", "${tasks.size}, display tasks: ${taskNames.size}")

        //populate the task spinner
        var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, taskNames)
        predecessorSpinner.adapter = adapter

        taskButton = rootView!!.findViewById(R.id.task_button) as Button


        taskButton.setOnClickListener {
            //the save button has been clicked, store or append the data
            //build the task object
            task = Task()
            task.setPred(predList)
            task.attribute.put(Task.NAME_COLUMN, taskName.text.toString())
            task.attribute.put(Task.DURATION_COLUMN, "${duration.text}")
            task.attribute.put(Task.DESCRIPTION_COLUMN, description.text.toString())
            task.addPred(selectedPredTask)


            if(validateTask(task)){
                //all information about the task is valid
                //then save the task to database
                fragmentEventListener.onAdd(task)
                //build a champion object
                var champion = Champion(championName.text.toString())
                if(validateChampion(champion)){
                    //the champion is valid so add it to the task and save the champion and task to the database
                    champion.assignedTasks.add(task)
                    fragmentEventListener.onAdd(champion)
                    task.attribute.put(Task.CHAMPION_COLUMN, champion.ID)
                    fragmentEventListener.onAdd(task)
                }
            }
            list.add(task)
        }
        return rootView
    }
    fun getTask(): Task{
        return this.task
    }

    //handle the user selection of predecessor task
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
