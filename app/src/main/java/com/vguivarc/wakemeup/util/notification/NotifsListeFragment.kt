package com.vguivarc.wakemeup.util.notification

import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

class NotifsListeFragment : Fragment(R.layout.fragment_notif_list), NotifsListeAdapter.NotifListAdapterListener {
    override fun onSenderClicked(sender: UserProfile) {
        TODO("Not yet implemented")
    }

    override fun onNotifDelete(notifKey: String) {
        TODO("Not yet implemented")
    }
/*
    private val authViewModel: AuthViewModel by sharedViewModel()
    private lateinit var adapter: NotifsListeAdapter
    private val notifs = mutableMapOf<String, Notif>()
    private val contacts = mutableMapOf<String, UserProfile>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO récupérer et afficher notifications
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_notif)
        adapter = NotifsListeAdapter(notifs, contacts, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun updateNotifListe(nouvelleListNotif: Map<String, Notif>) {
        notifs.clear()
        notifs.putAll(nouvelleListNotif)
        adapter.notifyDataSetChanged()
        // TODO add text si 0 notifs
    }

    private fun updateContactListe(nouvelleListContact: MutableMap<String, UserProfile>) {
        contacts.clear()
        contacts.putAll(nouvelleListContact)
        adapter.notifyDataSetChanged()
        // TODO add text si 0 notifs
    }

    override fun onSenderClicked(sender: UserProfile) {
       /* val action = NotifsListeFragmentDirections.actionNotifsListeFragmentToContactFragment(sender)
        findNavController().navigate(action)*/
    }

    override fun onNotifDelete(notifKey: String) {
        //  notifViewModel.deleteNotif(notifKey)
    }

 */
}
