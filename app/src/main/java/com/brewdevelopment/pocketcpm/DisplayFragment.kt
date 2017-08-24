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
import java.io.Serializable

/**
 * Created by neyon on 2017-07-27.
 * This fragment displays the different tasks that have been added by the user
 * triggered from the navigation drawer, and projects tab when individual project is selected
 */

class DisplayFragment(): Fragment(){

    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit var x: Project


    companion object {

        val PROJECT_KEY: String = "project"
        val TASK_KEY: String = "task"
        fun newInstance(Projectname: String, list: ArrayList<Task> ): DisplayFragment{//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment
            var args: Bundle = Bundle()
            args.putSerializable("lstTask", list as Serializable)
            val lstObj = args.getSerializable("lstTask") as ArrayList<Task>
            var fragment: DisplayFragment = DisplayFragment()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
            //makes call to the super's constructor &  can do processes before call

        }

        fun newInstance(list: ArrayList<Project>): DisplayFragment{
            var args: Bundle = Bundle()
            args.putSerializable("lstProject", list as Serializable)
            val lstObj = args.getSerializable("lstProject") as ArrayList<Project>
            var fragment: DisplayFragment = DisplayFragment()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
            //makes call to the super's constructor &  can do processes before call
        }
    }

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context as FragmentEventListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("WERE HERE","11")
        var i: Int=0
        val rootView = inflater?.inflate(R.layout.task_list, container, false)
        recyclerView= rootView?.findViewById(R.id.recycler_view) as RecyclerView

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        x = ProjAdapter(activity).list[position]
                        fragmentEventListener.onProjectSelect(x)
                        Log.e("@@@@@", "" + position)
                    }
                })
        )
        val fragment = fragmentManager.findFragmentById(R.id.content_frame)
        when(fragment.tag) {
            PROJECT_KEY -> {
                recyclerView?.adapter = ProjAdapter(activity)
                i++
                Log.e("WERE HERE", "55")
            }
            TASK_KEY -> {
                Log.e("WERE HERE", "")
                recyclerView?.adapter=RecyclerAdapter(activity,x)

            }

        }


        recyclerView?.layoutManager= LinearLayoutManager(activity)
        return rootView
    }


}