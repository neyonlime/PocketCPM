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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.Serializable

/**
 * Created by neyon on 2017-09-06.
 */
class Add_Champ_Frag(): Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var fragmentEventListener: FragmentEventListener
    lateinit var item: Champion
    private lateinit var task: Task
    private lateinit var project: Project
    lateinit var champName: TextView
    lateinit var dTxt: TextView
    lateinit var clist: ArrayList<Champion>
    init{
        task= Task()
        project=Project()
    }
    constructor(task: Task, project: Project):this(){
        this.task = task
        this.project=project
    }

    companion object {
        val ADD_CHAMP= "add champ"
        val CHAMP_LIST = "champlist"
        fun newInstance(list: ArrayList<Champion>): Add_Champ_Frag {//get appropriate arguments that are needed to construct the fragment
            //process and bundle up fragments before adding it to the fragement
            //the arguments will be bundles which will then be passed using setArguments(), to the fragment
            var args: Bundle = Bundle()
            args.putSerializable(CHAMP_LIST, list as Serializable)
            var fragment= Add_Champ_Frag()
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
        clist=fragmentManager.findFragmentById(R.id.content_frame).arguments.getSerializable(Add_Champ_Frag.CHAMP_LIST) as ArrayList<Champion>
        val rootView = inflater?.inflate(R.layout.champ_view, container, false)
        val addButton = rootView?.findViewById(R.id.champion_add_button) as Button
        champName= rootView?.findViewById(R.id.champion_name_field) as EditText
        recyclerView= rootView?.findViewById(R.id.task_recycler_view) as RecyclerView
        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        item = Champ_Adapter(activity,clist).list[position]
                        Log.e("@@@@@", "" + position)
                    }
                })
        )

        recyclerView.adapter= Champ_Adapter(activity,clist)
        recyclerView.layoutManager= LinearLayoutManager(activity)

        //on click l;istener to handle adding a champion
        addButton.setOnClickListener{
            val nameField = champName.text
            if(nameField.isNotEmpty()){
                //build champion
                var champion = Champion(nameField.toString())
                if(validateChampion(champion)){
                    fragmentEventListener.onAdd(champion)
                    clist.add(champion)
                    champName.setText("")
                }else{
                    //champion not added as it was not valid
                    champName.setText("")
                }
            }
            recyclerView.swapAdapter(Champ_Adapter(activity,clist), false)
        }

        return rootView
    }

    private fun validateChampion(champion: Champion): Boolean{
        if(champion.name.trim().isNotEmpty()){
            return true
        }
        return false
    }
}