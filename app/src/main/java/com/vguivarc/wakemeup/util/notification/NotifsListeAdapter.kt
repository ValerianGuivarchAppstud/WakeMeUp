package com.vguivarc.wakemeup.util.notification

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
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Notif
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

class NotifsListeAdapter(
    private val notifs: MutableMap<String, Notif>,
    private val contacts: MutableMap<String, UserProfile>,
    private val listener: NotifListAdapterListener?
) : RecyclerView.Adapter<NotifsListeAdapter.ViewHolder>(),
    View.OnClickListener {

    interface NotifListAdapterListener {
        fun onSenderClicked(sender: UserProfile)
        fun onNotifDelete(notifKey: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notifCardView = itemView.findViewById<CardView>(R.id.notif_card_view)!!
        val idItemNotifProfil = itemView.findViewById<ImageView>(R.id.id_item_notif_profil)!!
        var nomSenderNotifLink = itemView.findViewById<TextView>(R.id.nom_sender_notif_link)!!
        var idItemNotifTitreText = itemView.findViewById<TextView>(R.id.id_item_notif_titre_text)!!
        var idItemNotifDelete = itemView.findViewById<ImageButton>(R.id.id_item_notif_delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_notif, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = notifs.toList().size - position - 1
        val notif = notifs.toList()[index].second
        with(holder) {
            notifCardView.tag = notif
            if (notif.urlPicture != "") {
                Glide.with(AndroidApplication.appContext)
                    .load(notif.urlPicture)
                    .into(idItemNotifProfil)
            } else {
                idItemNotifProfil.setImageDrawable(
                    ContextCompat.getDrawable(AndroidApplication.appContext, R.drawable.empty_picture_profil)
                )
            }
            nomSenderNotifLink.text = "  ${notif.usernameSender}  "
            nomSenderNotifLink.setOnClickListener {
                if (notif.idSender != null && contacts.containsKey(notif.idSender)) {
                    listener?.onSenderClicked(contacts.get(notif.idSender)!!)
                }
            }
            idItemNotifTitreText.text = when (notif.type) {
                Notif.NotificationType.ENVOIE_MUSIQUE -> " t'a envoyé une sonnerie."
                Notif.NotificationType.SONNERIE_UTILISEE -> " a été réveillé par ta sonnerie : " + (notif.sonnerieName!!)
            }
            idItemNotifDelete.setOnClickListener {
                listener?.onNotifDelete(notifs.toList()[index].first)
            }
        }
    }

    override fun getItemCount(): Int = notifs.size

    override fun onClick(v: View) {
    }
}
