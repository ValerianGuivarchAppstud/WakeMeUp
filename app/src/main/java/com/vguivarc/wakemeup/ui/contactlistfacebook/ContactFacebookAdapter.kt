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
/*
class ContactFacebookAdapter(
    private val context: Context,
    private var contactFacebookList: List<ContactFacebook>,
    private val listener: ContactFacebookItemClickListener
) : RecyclerView.Adapter<ContactFacebookAdapter.SongViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_video, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {

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

        holder.tvTitle.text = contactFacebook.song.title

        if(contactFacebook.isFavorite) {
            Picasso.get().load(R.drawable.ic_favorite_yes)
                .into(holder.itemAddFavorite)
        } else {
            Picasso.get().load(R.drawable.ic_favorite_no)
                .into(holder.itemAddFavorite)
        }

        Picasso.get().load(contactFacebook.song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(holder.ivArtwork)
        holder.bind(listener)
    }

    override fun getItemCount(): Int {
        return contactFacebookList.size
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        val itemAddFavorite: ImageView = itemView.findViewById<View>(R.id.song_item_add_favorite) as ImageView
        val itemShare: ImageView = itemView.findViewById<View>(R.id.song_item_share) as ImageView

        fun bind(listener: ContactFacebookItemClickListener) {
            ivArtwork.setOnClickListener { listener.onSongPictureClickListener(layoutPosition) }
            itemAddFavorite.setOnClickListener { listener.onSongFavoriteClickListener(layoutPosition) }
            itemShare.setOnClickListener { listener.onSongShareClickListener(layoutPosition) }
        }
    }

    fun getSongWithPosition(position: Int): ContactFacebook {
        return contactFacebookList[position]
    }
}
*/