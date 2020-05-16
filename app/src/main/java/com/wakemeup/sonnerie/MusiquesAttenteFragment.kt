package com.wakemeup.sonnerie

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakemeup.R
import kotlinx.android.synthetic.main.fragment_musiques_attente.view.*

class MusiquesAttenteFragment : Fragment(), SonnerieAdapter.RecyclerItemClickListener {

    private val sonnerieAttenteList = mutableListOf<SonnerieRecue>()

    private lateinit var mAdapter: SonnerieAdapter
    private lateinit var currentView: View

    private lateinit var viewModel: MusiquesListesViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter =  SonnerieAdapter(this.requireContext(), sonnerieAttenteList,this)
        val recyclerView = currentView.recycler_list_video_musiques_en_attente
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        viewModel = ViewModelProvider(requireActivity()).get(MusiquesListesViewModel::class.java)
        viewModel.getListeAttenteLiveData().observe(viewLifecycleOwner, Observer { list ->
            updateAttenteListe(list)
        })
    }

    private fun updateAttenteListe(state: MusiquesListViewState) {
        Log.e("update", "updateAttenteListe = " + state.musiques)
        if (state.hasMusiquesChanged){
            sonnerieAttenteList.clear()
            sonnerieAttenteList.addAll(state.musiques.values)
            //todo tri par date
            sonnerieAttenteList.sortWith(compareBy { v -> v.sonnerieId })
            mAdapter.notifyDataSetChanged()
            currentView.texte_pas_de_musiques_en_attente.visibility = INVISIBLE
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_attente, container, false)
        return currentView
    }

    companion object {

        fun newInstance(ctx: Context): MusiquesAttenteFragment {
            return MusiquesAttenteFragment()
        }
        fun newInstance(): MusiquesAttenteFragment {
            return MusiquesAttenteFragment()
        }
    }

    override fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int) {

        //todo ouverture d'une fenetre d'options ?
    }
}