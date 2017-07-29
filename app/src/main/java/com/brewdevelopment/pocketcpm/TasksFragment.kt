package com.brewdevelopment.pocketcpm

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by neyon on 2017-07-27.
 */

class TasksFragment: Fragment(){

    companion object {
        val MESSAGE_KEY: String = "mKey"
        fun newInstance(title: String): Fragment{//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment

            var args: Bundle = Bundle()
            args.putString(MESSAGE_KEY,title)
            var fragment: TasksFragment = TasksFragment()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment

            //makes call to the super's constructor & i can do processes before call

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }
}