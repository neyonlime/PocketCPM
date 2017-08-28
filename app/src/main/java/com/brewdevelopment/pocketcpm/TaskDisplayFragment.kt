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
 * Created by ashkanabedian on 2017-08-24.
 */
class TaskDisplayFragment(): Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit var task: Task

    companion object {

        val TASK_KEY: String = "task"
        val TASK_LIST = "taskList"
        fun newInstance(list: ArrayList<Task>): TaskDisplayFragment {//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment
            var args: Bundle = Bundle()
            args.putSerializable(TASK_LIST, list as Serializable)
            var fragment= TaskDisplayFragment()
            fragment.arguments = args       //no getters or setters thus, setArgument -> .arguments
            return fragment
            //makes call to the super's constructor &  can do processes before call
        }
    }

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if(!(context is FragmentEventListener)) throw AssertionError()  //if the call activity has implemented AddFragmentEventListener continue
        fragmentEventListener = context
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.e("@@@@@", "JSIAJISOJIAOJSIOAS")
        val rootView = inflater?.inflate(R.layout.task_list, container, false)
        recyclerView= rootView?.findViewById(R.id.task_recycler_view) as RecyclerView
        var list = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(TASK_LIST) as ArrayList <Task>

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        task = RecyclerAdapter(activity, list).list[position]
                        fragmentEventListener.onTaskSelect(task)
                        Log.e("@@@@@", "" + position)
                    }
                })
        )
        recyclerView.adapter=RecyclerAdapter(activity, list)
        recyclerView.layoutManager= LinearLayoutManager(activity)

        return rootView
    }
}