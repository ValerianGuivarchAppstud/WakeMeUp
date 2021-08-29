package com.vguivarc.wakemeup.ui.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Contact

class ContactListAdapter(
    private var contactList: List<Contact>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    fun setContactList(contactList: List<Contact>) {
        this.contactList = contactList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_contact, parent, false)
        return ContactViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) {
        val contact = contactList[position]
        holder.itemContactName.text = contact.userProfile.nickname
        Picasso.get().load(contact.userProfile.imageUrl)
            .placeholder(R.drawable.empty_picture_profil)
            .into(holder.itemContactPicture)
        holder.bind(contactList[position], listener)
    }

    interface RecyclerItemClickListener {
        fun onContactListener(contact: Contact)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemContactCard = itemView.findViewById<CardView>(R.id.card_view_contact)
        val itemContactName = itemView.findViewById<TextView>(R.id.item_contact_name)
        val itemContactPicture = itemView.findViewById<ImageView>(R.id.item_contact_picture)
        val itemContactInfos = itemView.findViewById<TextView>(R.id.item_contact_infos)

        fun bind(contact: Contact, listener: RecyclerItemClickListener) {
            itemContactCard.setOnClickListener { listener.onContactListener(contact) }
        }
    }
}
