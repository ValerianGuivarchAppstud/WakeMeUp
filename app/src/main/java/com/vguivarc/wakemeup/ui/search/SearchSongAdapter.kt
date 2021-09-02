package com.vguivarc.wakemeup.ui.search

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

class SearchSongAdapter(
    private val context: Context,
    private val listener: SongItemClickListener
) : RecyclerView.Adapter<SearchSongAdapter.SongViewHolder>() {
    var selectedSong: SearchSong? = null
    private var songList: List<SearchSong> = listOf()

    fun setListSearchSong(listSearchSong: List<SearchSong>) {
        this.songList = listSearchSong
        notifyDataSetChanged()
    }

    interface SongItemClickListener {
        fun onSongPictureClickListener(position: Int)
        fun onSongFavoriteClickListener(position: Int)
        fun onSongShareClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_video, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {

        val searchSong = songList[position]
        if (selectedSong?.song?.id == searchSong.song.id) {
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

        holder.tvTitle.text = searchSong.song.title

        if(searchSong.isFavorite) {
            Picasso.get().load(R.drawable.ic_favorite_yes)
                .into(holder.itemAddFavorite)
        } else {
//            Picasso.get().load(R.drawable.ic_favorite_no)
//                .into(holder.itemAddFavorite)
        }

        Picasso.get().load(searchSong.song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(holder.ivArtwork)
        holder.bind(listener)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        val itemAddFavorite: ImageView = itemView.findViewById<View>(R.id.song_item_add_favorite) as ImageView
        val itemShare: ImageView = itemView.findViewById<View>(R.id.song_item_share) as ImageView

        fun bind(listener: SongItemClickListener) {
            ivArtwork.setOnClickListener { listener.onSongPictureClickListener(layoutPosition) }
            itemAddFavorite.setOnClickListener { listener.onSongFavoriteClickListener(layoutPosition) }
            itemShare.setOnClickListener { listener.onSongShareClickListener(layoutPosition) }
        }
    }

    fun getSongWithPosition(position: Int): SearchSong {
        return songList[position]
    }
}
