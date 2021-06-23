package com.vguivarc.wakemeup.ui.sonnerie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Ringing
import kotlinx.android.synthetic.main.fragment_musiques_attente.view.*

class MusiquesAttenteFragment : Fragment(), SonnerieAttenteAdapter.RecyclerItemClickListener {

    private val sonnerieAttenteList = mutableListOf<Ringing>()

    private lateinit var mAdapter: SonnerieAttenteAdapter
    private lateinit var currentView: View
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: SonnerieListeViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = SonnerieAttenteAdapter(sonnerieAttenteList, this)
        recyclerView = currentView.recycler_list_video_musiques_en_attente
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        viewModel.getListeAttenteLiveData().observe(
            viewLifecycleOwner,
            { list ->
                updateAttenteListe(list)
            }
        )
        viewModel.listeAffichee()
    }

    private fun updateAttenteListe(state: Map<String, Ringing>) {
        sonnerieAttenteList.clear()
        sonnerieAttenteList.addAll(state.values)
        // todo tri par date
        if (sonnerieAttenteList.size> 0) {
            sonnerieAttenteList.sortWith(compareBy { v -> v.dateSent })
            recyclerView.visibility = View.VISIBLE
            mAdapter.notifyDataSetChanged()
            currentView.texte_pas_de_musiques_en_attente.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            currentView.texte_pas_de_musiques_en_attente.visibility = View.VISIBLE
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

        fun newInstance(): MusiquesAttenteFragment {
            return MusiquesAttenteFragment()
        }
    }

    override fun onClickSonnerieListener(ringing: Ringing, position: Int) {
        // TO
        // todo ouverture d'une fenetre d'options ?
    }
}
