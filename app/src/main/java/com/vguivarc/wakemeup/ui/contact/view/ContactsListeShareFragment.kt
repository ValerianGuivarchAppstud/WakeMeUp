package com.vguivarc.wakemeup.ui.contact.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.ui.contact.ContactListeViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.ui.sonnerie.SonnerieListeViewModel

class ContactsListeShareFragment : Fragment(),
    ContactListeShareAdapter.ContactListShareAdapterListener {

    private lateinit var viewModelContact: ContactListeViewModel
    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var adapter: ContactListeShareAdapter
    private lateinit var loading: ProgressBar
    private lateinit var textePasDeContact: TextView
    private val contactsList = mutableMapOf<String, UserModel>()
    private val sonneriesEnvoyeesList = mutableListOf<Ringing>()
    private val sonneriesAttenteList = mutableListOf<Ringing>()
    private val sonneriesPasseList = mutableListOf<Ringing>()
    private val selections = mutableListOf<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val factory = ViewModelFactory(AndroidApplication.repository)
        viewModelContact = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)

        viewModelSonnerie.getSonneriesEnvoyees().observe(
            viewLifecycleOwner,
            {
                sonneriesEnvoyeesList.clear()
                sonneriesEnvoyeesList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        )
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

        viewModelContact.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            {
                if (it.error == null) {
                    if (it.friendList.isEmpty()) {
                        contactsList.clear()
                        adapter.notifyDataSetChanged()
                        textePasDeContact.visibility = View.VISIBLE
                    } else {
                        contactsList.clear()
                        contactsList.putAll(it.friendList)
                        adapter.notifyDataSetChanged()
                        textePasDeContact.visibility = View.GONE
                    }
                }
                loading.visibility = View.GONE
            })

        viewModelContact.getContacts()

        val view = inflater.inflate(R.layout.fragment_contact_share_list, container, false)
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

        return view
    }

    override fun onContactClicked(contact: UserModel, itemView: View) {
        if (selections.contains(contact)) {
            selections.remove(contact)
        } else {
            selections.add(contact)
        }
        adapter.notifyDataSetChanged()
    }

}
