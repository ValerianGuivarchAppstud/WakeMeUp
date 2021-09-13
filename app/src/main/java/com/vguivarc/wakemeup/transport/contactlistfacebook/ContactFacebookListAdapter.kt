package com.vguivarc.wakemeup.transport.contactlistfacebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook


class ContactFacebookListAdapter(
    private var contactFacebookList: List<ContactFacebook>,
    private val listener: ContactFacebookItemClickListener
) : RecyclerView.Adapter<ContactFacebookListAdapter.ContactFacebookViewHolder>() {
    var selectedPosition: Int = 0

    fun updateData(contactFacebookList: List<ContactFacebook>) {
        this.contactFacebookList = contactFacebookList
        notifyDataSetChanged()
    }

    interface ContactFacebookItemClickListener {
        fun onAddContactClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactFacebookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_contact_facebook, parent, false)
        return ContactFacebookViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactFacebookViewHolder, position: Int) {

        val contactFacebook = contactFacebookList[position]
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.colorPrimaryLight
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    android.R.color.transparent
                )
            )
        }
        if (contactFacebook.contact) {
            holder.fbAdd.setImageResource(R.drawable.ic_baseline_person_remove_24)
        } else {
            holder.fbAdd.setImageResource(R.drawable.ic_baseline_person_add_24)
        }

        holder.fbName.text = contactFacebook.username

        Picasso.get().load(contactFacebook.pictureUrl).placeholder(R.drawable.music_placeholder)
            .into(holder.fbPicture)
        holder.bind(listener)
    }

    override fun getItemCount(): Int {
        return contactFacebookList.size
    }

    fun getContactFacebookWithPosition(position: Int): ContactFacebook {
        return contactFacebookList[position]
    }

    class ContactFacebookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fbName: TextView = itemView.findViewById<View>(R.id.item_contact_facebook_name) as TextView
        val fbPicture: ImageView = itemView.findViewById<View>(R.id.item_contact_facebook_picture) as ImageView
        val fbAdd: ImageView = itemView.findViewById<View>(R.id.item_add_contact_facebook_picture) as ImageView
        fun bind(listener: ContactFacebookItemClickListener) {
            fbAdd.setOnClickListener { listener.onAddContactClickListener(layoutPosition) }
        }
    }
}
