package com.vguivarc.wakemeup.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.sonnerie.Sonnerie

class ContactListeAdapter(
    private val context: Context,
    private var contacts: Map<String, Contact>,
    private var sonneriesEnvoyees: MutableList<Sonnerie>,
    private var sonneriesAttente: MutableList<Sonnerie>,
    private var sonneriesPassees: MutableList<Sonnerie>,
    private val listener: ContactListAdapterListener?
) : RecyclerView.Adapter<ContactListeAdapter.ViewHolder>(),
    View.OnClickListener{

    interface ContactListAdapterListener {
        fun onContactClicked(contact: Contact, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_view_contact = itemView.findViewById<CardView>(R.id.card_view_contact)!!
        var item_contact_image = itemView.findViewById<ImageView>(R.id.item_contact_image)!!
        var item_contact_nom = itemView.findViewById<TextView>(R.id.item_contact_nom)!!
        var item_contact_musique_envoyee = itemView.findViewById<TextView>(R.id.item_contact_musique_envoyee)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_contact, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts.toList()[position].second
        with(holder) {
            card_view_contact.tag = contact
            card_view_contact.setOnClickListener(this@ContactListeAdapter)
            //item_contact_image.setImageDrawable((, null)))
            item_contact_nom.text = contact.user!!.username

            if(contact.user!!.imageUrl!="") {
                Glide.with(context)
                    .load(contact.user!!.imageUrl)
                    .into(item_contact_image)
            } else {
                item_contact_image.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.empty_picture_profil))
            }

            val recu = sonneriesAttente.filter { son -> son.senderId==contact.idContact }.size + sonneriesPassees.filter { son -> son.senderId==contact.idContact }.size
            val envoye = sonneriesEnvoyees.filter { son -> son.idReceiver==contact.idContact }.size
            item_contact_musique_envoyee.text = "Musique reçue : ${recu} - Musique envoyée : ${envoye}"
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        listener?.onContactClicked(v.tag as Contact, v)


    }
}