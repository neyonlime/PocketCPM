package com.brewdevelopment.pocketcpm

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by ashkanabedian on 2017-08-21.
 */

class AddProjectFragment(): Fragment(){

    private constructor(project: Project): this(){
        this.project = project
    }

    private lateinit var project: Project
    lateinit var fragmentEventListener: FragmentEventListener

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

        if(project !== null){
            //edit instance
            nameField.setText(project.name)
        }

        //add instance
        return rootView
    }

}