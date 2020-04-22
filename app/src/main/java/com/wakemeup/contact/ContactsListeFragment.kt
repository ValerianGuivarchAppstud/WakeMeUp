package com.wakemeup.contact

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Tag
import com.neocampus.repo.ViewModelFactory
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.UserModel

class ContactsListeFragment : Fragment(), ContactListeAdapter.ContactListAdapterListener {

    companion object {
        fun newInstance(): ContactsListeFragment {
            return ContactsListeFragment()
        }
    }

    private lateinit var viewModel: ContactListeViewModel
    private lateinit var adapter: ContactListeAdapter
    private val contacts = mutableMapOf<String, UserModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(AppWakeUp.repository)

        viewModel = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModel.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { nouvelleListeContacts ->
                updateContactsListe(
                    nouvelleListeContacts
                )
            })
    }

    private fun updateContactsListe(nouvelleListeContacts: Map<String, UserModel>) {
        contacts.clear()
        contacts.putAll(nouvelleListeContacts)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_contact)
        adapter = ContactListeAdapter(contacts, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        return view
    }

    override fun onContactClicked(userModel: UserModel, itemView: View) {
        //todo action click user
    }


}