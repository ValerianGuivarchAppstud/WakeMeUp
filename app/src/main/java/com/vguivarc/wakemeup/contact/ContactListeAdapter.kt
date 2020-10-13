package com.vguivarc.wakemeup.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.sonnerie.Sonnerie

class ContactListeAdapter(
    private val context: Context,
    private var contacts: Map<String, UserModel>,
    private var sonneriesEnvoyees: MutableList<Sonnerie>,
    private var sonneriesAttente: MutableList<Sonnerie>,
    private var sonneriesPassees: MutableList<Sonnerie>,
    private val listener: ContactListAdapterListener?
) : RecyclerView.Adapter<ContactListeAdapter.ViewHolder>(),
    View.OnClickListener{

    interface ContactListAdapterListener {
        fun onContactClicked(contact: UserModel, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardViewContact = itemView.findViewById<CardView>(R.id.card_view_contact)!!
        var itemContactImage = itemView.findViewById<ImageView>(R.id.item_contact_image)!!
        var itemContactNom = itemView.findViewById<TextView>(R.id.item_contact_nom)!!
        var itemContactMusiqueEnvoyee = itemView.findViewById<TextView>(R.id.item_contact_musique_envoyee)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_contact, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts.toList()[position].second
        with(holder) {
            cardViewContact.tag = contact
            cardViewContact.setOnClickListener(this@ContactListeAdapter)
            //item_contact_image.setImageDrawable((, null)))
            itemContactNom.text = contact.username

            if(contact.imageUrl!="") {
                Glide.with(context)
                    .load(contact.imageUrl)
                    .into(itemContactImage)
            } else {
                itemContactImage.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.empty_picture_profil))
            }

            val recu = sonneriesAttente.filter { son -> son.senderId==contact.id }.size + sonneriesPassees.filter { son -> son.senderId==contact.id }.size
            val envoye = sonneriesEnvoyees.filter { son -> son.idReceiver==contact.id }.size
            val txtEnvoye = AppWakeUp.appContext.resources.getQuantityString(R.plurals.musique_envoyée, recu)
            val txtRecu = AppWakeUp.appContext.resources.getQuantityString(R.plurals.musique_recu, envoye)
            val final = "$txtEnvoye $envoye - $txtRecu $recu"

            itemContactMusiqueEnvoyee.text =final
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        listener?.onContactClicked(v.tag as UserModel, v)


    }
}