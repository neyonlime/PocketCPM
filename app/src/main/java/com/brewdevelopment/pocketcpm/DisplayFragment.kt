package com.brewdevelopment.pocketcpm

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by neyon on 2017-07-27.
 * This fragment displays the different tasks that have been added by the user
 * triggered from the navigation drawer, and projects tab when individual project is selected
 */

class DisplayFragment: Fragment(){

    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener

    companion object {
        val MESSAGE_KEY: String = "mKey"
        fun newInstance(title: String, list: ArrayList<Task> ): Fragment{//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment

            var args: Bundle = Bundle()
            args.putString(MESSAGE_KEY,title)
            var fragment: DisplayFragment = DisplayFragment()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
            //makes call to the super's constructor &  can do processes before call
        }
        fun newInstance(list: ArrayList<Project>){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context as FragmentEventListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.task_list, container, false)
        recyclerView= rootView?.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView?.layoutManager= LinearLayoutManager(activity)
        recyclerView?.adapter= RecyclerAdapter(recyclerView)
        return rootView
    }


}