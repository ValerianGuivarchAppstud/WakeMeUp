package com.vguivarc.wakemeup.notification

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
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.ui.contact.ContactListeViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory

class NotifsListeFragment : Fragment(), NotifsListeAdapter.NotifListAdapterListener {


    private lateinit var notifViewModel: NotifListeViewModel
    private lateinit var contactViewModel: ContactListeViewModel
    private lateinit var adapter: NotifsListeAdapter
    private val notifs = mutableMapOf<String, NotificationMusicMe>()
    private val contacts = mutableMapOf<String, UserModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        val factory = ViewModelFactory(AndroidApplication.repository)
        notifViewModel = ViewModelProvider(this, factory).get(NotifListeViewModel::class.java)
        notifViewModel.notifAffichee()
        notifViewModel.getNotifLiveData().observe(
            viewLifecycleOwner,
            { nouvelleListeNotif ->
                updateNotifListe(
                    nouvelleListeNotif
                )
            })

        contactViewModel = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        contactViewModel.getContacts()
        contactViewModel.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            { nouvelleListeContact ->
                updateContactListe(
                    nouvelleListeContact.friendList
                )
            })

    }


    private fun updateNotifListe(nouvelleListNotif: Map<String, NotificationMusicMe>) {
        notifs.clear()
        notifs.putAll(nouvelleListNotif)
        adapter.notifyDataSetChanged()
        //TODO add text si 0 notifs

    }



    private fun updateContactListe(nouvelleListContact: MutableMap<String, UserModel>) {
        contacts.clear()
        contacts.putAll(nouvelleListContact)
        adapter.notifyDataSetChanged()
        //TODO add text si 0 notifs

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_notif_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_notif)
        adapter = NotifsListeAdapter(notifs, contacts, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        requireActivity().title = "Notifications"
        return view
    }

    override fun onSenderClicked(sender: UserModel) {
       /* val action = NotifsListeFragmentDirections.actionNotifsListeFragmentToContactFragment(sender)
        findNavController().navigate(action)*/
    }

    override fun onNotifDelete(notifKey: String) {
        notifViewModel.deleteNotif(notifKey)
    }

}