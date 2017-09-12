package com.brewdevelopment.pocketcpm

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*

/**
 * Created by ashkanabedian on 2017-08-21.
 */

class AddProjectFragment(): Fragment(), DatePickerDialog.OnDateSetListener{

    private constructor(project: Project): this(){
        this.project = project
    }

    private var project: Project? = null
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit private var startDateView: EditText

    companion object {
        val ADD_PROJECT ="add_project"
        val EDIT_PROJECT = "edit_project"

        fun newAddInstance(): AddProjectFragment{

            val fragment = AddProjectFragment()
            return fragment

        }

        fun newEditInstance(obj: Project): AddProjectFragment{
            //editing existing project
            val fragment = AddProjectFragment(obj)
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context as FragmentEventListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater?.inflate(R.layout.fragment_add_project,container, false)
        val nameField = rootView!!.findViewById(R.id.project_name_field) as EditText
        val button = rootView!!.findViewById(R.id.project_save_button) as Button
        startDateView = rootView!!.findViewById(R.id.start_date_field) as EditText

        if(project !== null){
            startDateView.setText(project!!.start)
            nameField.setText(project!!.name)
        }

        startDateView.setOnClickListener{
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(fragmentManager, "Start Date")
        }

        button.setOnClickListener{
            if(project === null){
                var project = Project("" + nameField.text)
                project.start = startDateView.text.toString()
                if(validateProject(project)){
                    nameField.setText("")
                    fragmentEventListener.onAdd(project)
                }else {
                    //send error message

                }
            }
            else if(project !== null){
                //edit instance
                project!!.name = "${nameField.text}"
                project!!.start = startDateView.text.toString()
                if(validateProject(project!!)){
                    nameField.setText("")
                    startDateView.setText("")
                    fragmentEventListener.onUpdate(project!!)
                }else{
                    //send error message

                }
            }
        }



        //add instance
        return rootView
    }

    private fun validateProject(project: Project): Boolean{
        return project.name.trim() != "" && project.start.trim() != ""
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var startDate = "" + dayOfMonth
        startDate+= "/" + "${monthOfYear+1}"
        startDate+= "/" + year

        startDateView.setText(startDate)
    }
}