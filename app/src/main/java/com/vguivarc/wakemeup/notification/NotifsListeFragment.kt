package com.vguivarc.wakemeup.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel

class NotifsListeFragment : Fragment(), NotifsListeAdapter.NotifListAdapterListener {


    private lateinit var viewModel: NotifListeViewModel
    private lateinit var adapter: NotifsListeAdapter
    private val notifs = mutableMapOf<String, NotificationMusicMe>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModel = ViewModelProvider(this, factory).get(NotifListeViewModel::class.java)
        viewModel.notifAffichee()
        viewModel.getNotifLiveData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { nouvelleListeNotif ->
                updateNotifListe(
                    nouvelleListeNotif
                )
            })
    }


    private fun updateNotifListe(nouvelleListNotif: Map<String, NotificationMusicMe>) {
        notifs.clear()
        notifs.putAll(nouvelleListNotif)
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
        adapter = NotifsListeAdapter(notifs, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        requireActivity().setTitle("Notifications")
        return view
    }

    override fun onSenderClicked(sender: UserModel) {
        val action = NotifsListeFragmentDirections.actionNotifsListeFragmentToContactFragment(sender)
        findNavController().navigate(action)
    }

    override fun onNotifDelete(notifKey: String) {
        viewModel.deleteNotif(notifKey)
    }

}