package com.vguivarc.wakemeup.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R

class NotifsListeAdapter(
    private val notifs: MutableMap<String, NotificationMusicMe>,
    private val listener: NotifListAdapterListener?
) : RecyclerView.Adapter<NotifsListeAdapter.ViewHolder>(),
    View.OnClickListener {

    interface NotifListAdapterListener {
        fun onSenderClicked(sender: FirebaseUser)
        fun onNotifDelete(notifKey: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notif_card_view = itemView.findViewById<CardView>(R.id.notif_card_view)!!
        val id_item_notif_profil = itemView.findViewById<ImageView>(R.id.id_item_notif_profil)!!
        var nom_sender_notif_link = itemView.findViewById<TextView>(R.id.nom_sender_notif_link)!!
        var id_item_notif_titre_text = itemView.findViewById<TextView>(R.id.id_item_notif_titre_text)!!
        var id_item_notif_delete = itemView.findViewById<ImageButton>(R.id.id_item_notif_delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_notif, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = notifs.toList().size-position-1
        val notif = notifs.toList()[index].second
        with(holder) {
            notif_card_view.tag = notif
            if(notif.sender!!.photoUrl.toString()!="") {
                Glide.with(AppWakeUp.appContext)
                    .load(notif.sender!!.photoUrl)
                    .into(id_item_notif_profil)
            } else {
                id_item_notif_profil.setImageDrawable(
                    ContextCompat.getDrawable(AppWakeUp.appContext, R.drawable.empty_picture_profil))
            }
            nom_sender_notif_link.text = "  "+notif.sender!!.displayName+"  "
            nom_sender_notif_link.setOnClickListener {
                listener?.onSenderClicked(notif.sender!!)
            }
            id_item_notif_titre_text.text = when(notif.type){
                NotificationMusicMe.NotificationType.AJOUT_CONTACT -> " t'a rajouté dans ses contacts."
                NotificationMusicMe.NotificationType.ENVOIE_MUSIQUE ->" t'a envoyé une sonnerie."
                NotificationMusicMe.NotificationType.SONNERIE_UTILISEE ->" a été réveillé par ta sonnerie : "+(notif.sonnerieName!!)
            }
            id_item_notif_delete.setOnClickListener {
                listener?.onNotifDelete( notifs.toList()[index].first)
            }
        }
    }

    override fun getItemCount(): Int = notifs.size

    override fun onClick(v: View) {
    }
}