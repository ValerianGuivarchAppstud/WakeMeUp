package com.vguivarc.wakemeup.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.sonnerie.Sonnerie
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility

class ContactsListeShareFragment : Fragment(),
    ContactListeShareAdapter.ContactListShareAdapterListener {

    //TODO item musique cherché enlever durée

    private lateinit var viewModelContact: ContactListeViewModel
    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var adapter: ContactListeShareAdapter
    private lateinit var loading: ProgressBar
    private lateinit var textePasDeContact: TextView
    private val contactsList = mutableMapOf<String, Contact>()
    private val sonneriesEnvoyeesList = mutableListOf<Sonnerie>()
    private val sonneriesAttenteList = mutableListOf<Sonnerie>()
    private val sonneriesPasseList = mutableListOf<Sonnerie>()
    private val selections = mutableListOf<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelContact = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)

        viewModelSonnerie.getSonneriesEnvoyees().observe(
            viewLifecycleOwner,
            Observer {
                sonneriesEnvoyeesList.clear()
                sonneriesEnvoyeesList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        )
        viewModelSonnerie.getListeAttenteLiveData().observe(
            viewLifecycleOwner,
            Observer {
                sonneriesAttenteList.clear()
                sonneriesAttenteList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )
        viewModelSonnerie.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            Observer {
                sonneriesPasseList.clear()
                sonneriesPasseList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )

        viewModelContact.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            Observer { it ->
                if (it.error == null) {
                    if (it.contactList.size == 0) {
                        contactsList.clear()
                        adapter.notifyDataSetChanged()
                        textePasDeContact.visibility = View.VISIBLE
                    } else {
                        contactsList.clear()
                        contactsList.putAll(it.contactList)
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


        val song = ContactsListeShareFragmentArgs.fromBundle(requireArguments()).songToShare
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(view.findViewById<ImageView>(R.id.share_music_image))
        view.findViewById<TextView>(R.id.share_music_titre).setText(song.title)

        view.findViewById<Button>(R.id.valid_share_contact_button).setOnClickListener {
            for (c in selections) {
                viewModelSonnerie.addSonnerieToUser(song, c.user!!)
            }
            if (selections.size > 0) {
                Utility.createSimpleToast("Sonneries envoyées !")
                requireActivity().onBackPressed()
            } else {
                Utility.createSimpleToast("Veuillez sélectionner les contacts à qui envoyer la sonnerie")
            }
        }

        return view
    }

    override fun onContactClicked(contact: Contact, itemView: View) {
        if (selections.contains(contact)) {
            selections.remove(contact)
        } else {
            selections.add(contact)
        }
        adapter.notifyDataSetChanged()
    }

}
