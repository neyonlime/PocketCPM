package com.brewdevelopment.pocketcpm


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by neyon on 2017-07-29.
 * this fragment handels adding new tasks
 * gets passed the Activity of the task that called the fragment if it is being called for an edit
 */

class AddTaskFragement : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {

    val START_DATE = "start_date"
    val END_DATE = "end_date"
    val START_TIME = "start_time"
    val END_TIME="end_time"

    lateinit private var taskButton: Button
    lateinit private var startDateView: EditText
    lateinit private var endDateView: EditText
    lateinit private var selectedPredTask: Task             //predecessor task
    lateinit var startTime: String
    lateinit var endTime: String

    lateinit var fragmentEventListener: FragmentEventListener


    companion object {
        val ADD_TASK = "add_task"
        val CHAMPION_LIST = "champion_list"
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
        var rootView = inflater?.inflate(R.layout.fragement_add_task,container, false)
        startDateView = rootView!!.findViewById(R.id.start_date_field) as EditText
        endDateView = rootView!!.findViewById(R.id.end_date_field) as EditText
        val taskName = rootView!!.findViewById(R.id.task_name_field) as EditText
        val championName = rootView!!.findViewById(R.id.champion_name_field) as EditText
        val predecessorSpinner = rootView!!.findViewById(R.id.predecessor_spinner) as Spinner
        val description = rootView.findViewById(R.id.description_field) as EditText
        selectedPredTask = Task()

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

        startDateView.setOnClickListener{
            //define the date pop up and right after define the time popup
            //we can use 'it' to refer to the calling view

            val now: Calendar = Calendar.getInstance()
            var dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR),now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            dpd.show(fragmentManager, START_DATE)
            dpd.setVersion(DatePickerDialog.Version.VERSION_2)
        }

        endDateView.setOnClickListener {
            //define the date pop up and right after define the time popup
            //we can use 'it' to refer to the calling view

            val now: Calendar = Calendar.getInstance()
            var dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR),now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            dpd.show(fragmentManager, END_DATE)
            dpd.setVersion(DatePickerDialog.Version.VERSION_2)
        }

        taskButton.setOnClickListener {
            //the save button has been clicked, store or append the data
            //build the task object

            var task = Task()
            task.attribute.put(Task.NAME_COLUMN, taskName.text.toString())
            task.attribute.put(Task.START_COLUMN, "${startDateView.text}|${startTime}")
            task.attribute.put(Task.END_COLUMN, "${endDateView.text}|${endTime}")
            task.attribute.put(Task.DESCRIPTION_COLUMN, description.text.toString())
            task.setPred(selectedPredTask)

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
        }
        return rootView
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        if(view!!.tag == START_DATE){
            //START date entered
            Log.i("Dbg", view.tag)
            var startDate = "" + dayOfMonth
            startDate+= "/" + monthOfYear
            startDate+= "/" + year

            startDateView.setText(startDate)

            //get the start time
            val now: Calendar = Calendar.getInstance()
            val dpd = TimePickerDialog.newInstance(this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true)
            dpd.show(fragmentManager,START_TIME)
            dpd.setVersion(TimePickerDialog.Version.VERSION_2)
        }else if (view!!.tag == END_DATE){
            //END date entered
            var endDate = "" + dayOfMonth
            endDate+= "/" + monthOfYear
            endDate+= "/" + year

            endDateView.setText(endDate)

            //get the start time
            val now: Calendar = Calendar.getInstance()
            val dpd = TimePickerDialog.newInstance(this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true)
            dpd.show(fragmentManager,END_TIME)
            dpd.setVersion(TimePickerDialog.Version.VERSION_2)
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {

        if(view!!.tag == START_TIME){
            //START time entered
            startTime = "${hourOfDay}:${minute}:${second}"

        }else if(view!!.tag == END_TIME){
            //END time entered
            endTime = "${hourOfDay}:${minute}:${second}"
        }
    }


}
