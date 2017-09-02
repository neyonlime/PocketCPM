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

/**
 * Created by Osama on 2017-09-01.
 */
class TaskViewFragment: Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit var task: mCrit

    companion object {
        fun newInstance(): TaskViewFragment {//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment
            var args: Bundle = Bundle()
            var fragment= TaskViewFragment()
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
        val rootView = inflater?.inflate(R.layout.task_on_click_view, container, false)
        recyclerView= rootView?.findViewById(R.id.task_recycler_view) as RecyclerView
        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        task = onClickTaskAdapter(activity).list[position]
                        Log.e("@@@@@", "" + position)
                    }
                })
        )
        recyclerView.adapter=onClickTaskAdapter(activity)
        recyclerView.layoutManager= LinearLayoutManager(activity)

        return rootView
    }
}