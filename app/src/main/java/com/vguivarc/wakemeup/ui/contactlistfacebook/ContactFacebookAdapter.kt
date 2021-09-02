package com.vguivarc.wakemeup.ui.contactlistfacebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R

import com.vguivarc.wakemeup.domain.entity.ContactFacebook


class ContactFacebookAdapter(
    private val context: Context,
    private var contactFacebookList: List<ContactFacebook>,
    private val listener: ContactFacebookItemClickListener
) : RecyclerView.Adapter<ContactFacebookAdapter.ContactFacebookViewHolder>() {
    var selectedPosition: Int = 0

    fun setListContactFacebook(listContactFacebook: List<ContactFacebook>) {
        this.contactFacebookList = listContactFacebook
        notifyDataSetChanged()
    }

    interface ContactFacebookItemClickListener {
//        fun onSongPictureClickListener(position: Int)
//        fun onSongFavoriteClickListener(position: Int)
//        fun onSongShareClickListener(position: Int)
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
                    context,
                    R.color.colorPrimaryLight
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
        }

        holder.fbName.text = contactFacebook.name
/*
        if(contactFacebook.isFavorite) {
            Picasso.get().load(R.drawable.ic_favorite_yes)
                .into(holder.itemAddFavorite)
        } else {
            Picasso.get().load(R.drawable.ic_favorite_no)
                .into(holder.itemAddFavorite)
        }*/

        Picasso.get().load(contactFacebook.picture).placeholder(R.drawable.music_placeholder)
            .into(holder.fbPicture)
        holder.bind(listener)
    }

    override fun getItemCount(): Int {
        return contactFacebookList.size
    }

    class ContactFacebookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fbName: TextView = itemView.findViewById<View>(R.id.item_contact_facebook_name) as TextView
        val fbPicture: ImageView = itemView.findViewById<View>(R.id.item_contact_facebook_picture) as ImageView
        fun bind(listener: ContactFacebookItemClickListener) {
  /*          ivArtwork.setOnClickListener { listener.onSongPictureClickListener(layoutPosition) }
            itemAddFavorite.setOnClickListener { listener.onSongFavoriteClickListener(layoutPosition) }
            itemShare.setOnClickListener { listener.onSongShareClickListener(layoutPosition) }*/
        }
    }

    fun getSongWithPosition(position: Int): ContactFacebook {
        return contactFacebookList[position]
    }
}
