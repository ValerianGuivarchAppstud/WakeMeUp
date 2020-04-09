package com.wakemeup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.amis.AmiAdapter
import kotlinx.android.synthetic.main.activity_liste_ami.*
import kotlinx.android.synthetic.main.fragment_amis.view.*
import kotlinx.android.synthetic.main.fragment_amis_non_connect.*

class AmisFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (AppWakeUp.auth.currentUser!!.isAnonymous) {
            val currentView = inflater.inflate(R.layout.fragment_amis_non_connect, container, false)
            button_amis_connect.setOnClickListener {
                (activity as MainActivity).startConnectActivity(false)
            }
            currentView
        } else {
            val currentView = inflater.inflate(R.layout.fragment_amis, container, false)
            currentView.bouton_amis_validation.visibility = GONE

            currentView.recycler_list_ami.layoutManager = LinearLayoutManager(activity)
            currentView.recycler_list_ami.adapter =
                AmiAdapter(activity!!, AppWakeUp.listeAmis, listeSelection, this)
            currentView
        }
    }

    companion object {
        fun newInstance(): AmisFragment {
            return AmisFragment()
        }
    }

    override fun onClick(view: View) {
        if (view.tag != null) {
            val index = view.tag as Int

            if (listeSelection.contains(index)) {
                listeSelection.remove(index)
            } else {
                listeSelection.add(index)
            }
            recycler_list_ami.adapter?.notifyDataSetChanged()
        }

    }

    private val listeSelection: MutableList<Int> = mutableListOf(1)


}