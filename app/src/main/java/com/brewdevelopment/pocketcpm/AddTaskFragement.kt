package com.brewdevelopment.pocketcpm

import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker

/**
 * Created by neyon on 2017-07-29.
 * this fragment handels adding new tasks
 * gets passed the Activity of the task that called the fragment if it is being called for an edit
 */

class AddTaskFragement : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    companion object {
        fun newInstance(){
            //creating a brand NEW task

        }

        fun newInstance(callingTask: Task){
            //Being called from a task for an EDIT
            //create and pass the bundle containing the attributes of the task

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater?.inflate(R.layout.fragement_add_task,container, false)
        return rootView
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}
