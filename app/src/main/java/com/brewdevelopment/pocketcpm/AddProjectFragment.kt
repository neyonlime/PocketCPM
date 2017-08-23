package com.brewdevelopment.pocketcpm

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by ashkanabedian on 2017-08-21.
 */

class AddProjectFragment(): Fragment(){

    lateinit var fragmentEventListener: FragmentEventListener

    companion object {
        fun newInstance(): Fragment{
            //adding a new project
            val fragment = AddTaskFragement()
            return fragment
        }

        fun newInstance(project: Project): Fragment{
            //editing existing project
            val fragment = AddTaskFragement()
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context as FragmentEventListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater?.inflate(R.layout.fragement_add_task,container, false)
        val nameField = rootView!!.findViewById(R.id.project_name_field)
        return rootView
    }
}