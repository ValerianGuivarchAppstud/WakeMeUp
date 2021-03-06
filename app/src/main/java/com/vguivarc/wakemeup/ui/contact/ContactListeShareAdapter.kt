package com.vguivarc.wakemeup.ui.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.entity.UserProfile

class ContactListeShareAdapter(
    private val context: Context,
    private var contacts: Map<String, UserProfile>,
    private var selection: List<UserProfile>,
    private var sonneriesEnvoyees: MutableList<Ringing>,
    private var sonneriesAttente: MutableList<Ringing>,
    private var sonneriesPassees: MutableList<Ringing>,
    private val listener: ContactListShareAdapterListener?
) : RecyclerView.Adapter<ContactListeShareAdapter.ViewHolder>(),
    View.OnClickListener {

    interface ContactListShareAdapterListener {
        fun onContactClicked(contact: UserProfile, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemContactImage = itemView.findViewById<ImageView>(R.id.item_contact_picture)!!
        var itemContactNom = itemView.findViewById<TextView>(R.id.item_contact_name)!!
        var itemContactMusiqueEnvoyee =
            itemView.findViewById<TextView>(R.id.item_contact_infos)!!
        var checkBoxContact = itemView.findViewById<CheckBox>(R.id.checkBoxContact)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_contact, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts.toList()[position].second
        with(holder) {
            // item_contact_image.setImageDrawable((, null)))
            itemContactNom.text = contact.nickname

            if (contact.imageUrl != "") {
                Glide.with(context)
                    .load(contact.imageUrl)
                    .into(itemContactImage)
            } else {
                itemContactImage.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.empty_picture_profil)
                )
            }

            val recu =
                sonneriesAttente.filter { son -> son.senderId == contact.profileId }.size + sonneriesPassees.filter { son -> son.senderId == contact.profileId }.size
            val envoye =
                sonneriesEnvoyees.filter { son -> son.idReceiver == contact.profileId }.size
            itemContactMusiqueEnvoyee.text = AndroidApplication.appContext.resources.getString(
                R.string.txt_trait_txt,
                AndroidApplication.appContext.resources.getQuantityString(R.plurals.musique_envoy??e, recu),
                AndroidApplication.appContext.resources.getQuantityString(R.plurals.musique_recu, envoye)
            )
            checkBoxContact.isSelected = selection.contains(contact)
            checkBoxContact.visibility = View.VISIBLE
            checkBoxContact.tag = contact
            checkBoxContact.setOnClickListener(this@ContactListeShareAdapter)
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        listener?.onContactClicked(v.tag as UserProfile, v)
    }
}
