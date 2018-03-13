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
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable

/**
 * Created by ashkanabedian on 2017-08-24.
 */
class ProjectDisplayFragment(): Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit var prorject: Project

    companion object {
        val PROJECT_KEY: String = "project"
        val PROJECT_LIST = "projectList"
        fun newInstance(list: ArrayList<Project>): ProjectDisplayFragment{
            var args: Bundle = Bundle()
            args.putSerializable(PROJECT_LIST, list as Serializable)
            var fragment = ProjectDisplayFragment()
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

        val rootView = inflater?.inflate(R.layout.project_list, container, false)
        recyclerView= rootView?.findViewById(R.id.project_recycler_view) as RecyclerView
        var list = fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(PROJECT_LIST) as ArrayList <Project>
        val options = rootView.findViewById(R.id.options_button)
        val logo = rootView.findViewById(R.id.logo) as ImageView
        val info = rootView.findViewById(R.id.info_text) as TextView

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        prorject = ProjAdapter(activity, list).list[position]
                        fragmentEventListener.onProjectSelect(prorject)
                        Log.e("@@@@@", "" + position)
                    }
                })
        )

        recyclerView.adapter = ProjAdapter(activity, list)
        recyclerView.layoutManager= LinearLayoutManager(activity)

        if(list.isNotEmpty()){
            logo.visibility = View.GONE
            info.visibility = View.GONE
        }
        return rootView
    }
}