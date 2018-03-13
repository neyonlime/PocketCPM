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
    var editChamp: Champion? = null
    lateinit var dTxt: TextView
    lateinit var clist: ArrayList<Champion>
    init{
        task= Task()
        project=Project()
    }
    constructor(champion: Champion):this(){
        this.task = task
        this.editChamp = champion
    }

    companion object {
        val ADD_CHAMP= "add champ"
        val EDIT_CHAMP = "editChamp"
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
        fun newInstance(champion: Champion, list: ArrayList<Champion>): Add_Champ_Frag{
            var args = Bundle()
            args.putSerializable(CHAMP_LIST, list as Serializable)
            var fragment = Add_Champ_Frag(champion)
            fragment.arguments = args
            return fragment

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

        if(editChamp !== null){
            addButton.setText("Edit")
            champName.setText(editChamp!!.name)
        }

        //on click l;istener to handle adding a champion
        addButton.setOnClickListener{
            if(editChamp === null){
                val nameField = champName.text
                if(nameField.isNotEmpty()){
                    //build champion
                    var champion = Champion(nameField.toString().trim())
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
            }else{
                editChamp!!.name = champName.text.toString().trim()
                if(editChamp!!.name.isNotEmpty()){
                    if(validateChampion(editChamp!!)){
                        val position = getPosition(editChamp!!, clist)
                        if(position != -1){
                            clist.get(position).name = editChamp!!.name
                        }
                        fragmentEventListener.onUpdate(editChamp!!)
                        champName.setText("")
                    }else{
                        champName.setText("")
                    }
                }
                fragmentEventListener.onRefreshChampion()
            }

        }

        return rootView
    }

    private fun getPosition(champion: Champion, list: ArrayList<Champion>): Int{
        for(i in 0..list.size-1){
            if(champion.ID == list.get(i).ID){
                return i
            }
        }
        return -1
    }

    private fun validateChampion(champion: Champion): Boolean{
        if(champion.name.trim().isNotEmpty()){
            return true
        }
        return false
    }
}