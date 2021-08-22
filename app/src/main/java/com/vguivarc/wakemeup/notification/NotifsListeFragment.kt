package com.vguivarc.wakemeup.notification

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.domain.entity.Notif
import com.vguivarc.wakemeup.domain.entity.UserProfile
import com.vguivarc.wakemeup.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NotifsListeFragment : BaseLceFragment(R.layout.fragment_notif_list), NotifsListeAdapter.NotifListAdapterListener {

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
}
