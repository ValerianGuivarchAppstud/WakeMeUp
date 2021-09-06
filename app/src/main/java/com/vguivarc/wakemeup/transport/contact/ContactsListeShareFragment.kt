package com.vguivarc.wakemeup.transport.contact

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.transport.sonnerie.SonnerieListeViewModel

class ContactsListeShareFragment :
    Fragment(),
    ContactListeShareAdapter.ContactListShareAdapterListener {

    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var adapter: ContactListeShareAdapter
    private lateinit var loading: ProgressBar
    private lateinit var textePasDeContact: TextView
    private val contactsList = mutableMapOf<String, UserProfile>()
    private val sonneriesEnvoyeesList = mutableListOf<Ringing>()
    private val sonneriesAttenteList = mutableListOf<Ringing>()
    private val sonneriesPasseList = mutableListOf<Ringing>()
    private val selections = mutableListOf<UserProfile>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelSonnerie.getListeAttenteLiveData().observe(
            viewLifecycleOwner,
            {
                sonneriesAttenteList.clear()
                sonneriesAttenteList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )
        viewModelSonnerie.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            {
                sonneriesPasseList.clear()
                sonneriesPasseList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )
        textePasDeContact = view.findViewById(R.id.textPasContact)
        loading = view.findViewById(R.id.pb_main_loader)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_list_contact)
        adapter = ContactListeShareAdapter(
            requireContext(),
            contactsList,
            selections,
            sonneriesEnvoyeesList,
            sonneriesAttenteList,
            sonneriesPasseList,
            this
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

       /* val song = ContactsListeShareFragmentArgs.fromBundle(requireArguments()).songToShare
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(view.findViewById<ImageView>(R.id.share_music_image))
        view.findViewById<TextView>(R.id.share_music_titre).text = song.title

        view.findViewById<Button>(R.id.valid_share_contact_button).setOnClickListener {
            for (c in selections) {
                viewModelSonnerie.addSonnerieToUser(song, c)
            }
            if (selections.size > 0) {
                Utility.createSimpleToast("Sonneries envoyées !")
                requireActivity().onBackPressed()
            } else {
                Utility.createSimpleToast("Veuillez sélectionner les contacts à qui envoyer la sonnerie")
            }
        }*/
    }

    override fun onContactClicked(contact: UserProfile, itemView: View) {
        if (selections.contains(contact)) {
            selections.remove(contact)
        } else {
            selections.add(contact)
        }
        adapter.notifyDataSetChanged()
    }
}
