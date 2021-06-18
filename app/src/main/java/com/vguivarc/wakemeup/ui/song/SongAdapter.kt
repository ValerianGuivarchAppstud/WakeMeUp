package com.vguivarc.wakemeup.ui.song

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hsuaxo.rxtube.YTResult
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R

class SongAdapter(
    private val context: Context,
    private var songList: List<Song>,
    private val listener: SongItemClickListener
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    var selectedPosition: Int = 0

    fun setYTResult(ytResult: YTResult) {
        this.songList = ytResult.items.map { Song(it) }
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

        val song = songList[position]
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
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

        holder.tvTitle.text = song.title
        //       holder.tvArtist.text = song.artist
//        val duration = Utility.convertDuration(song.duration)

        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder)
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

    fun getSongWithPosition(position: Int): Song {
        return songList[position]
    }
}
