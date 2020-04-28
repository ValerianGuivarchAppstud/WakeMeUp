package com.wakemeup.song

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wakemeup.R
import com.wakemeup.util.Utility


class SongAdapter(
    private val context: Context,
    private val songList: MutableList<Song>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    var selectedPosition: Int = 0

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
        holder.tvArtist.text = song.artist
        val duration = Utility.convertDuration(song.duration)

        holder.tvDuration.text = duration

        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(holder.ivArtwork)
        holder.bind(song, listener)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val tvArtist: TextView = itemView.findViewById<View>(R.id.tv_artist) as TextView
        val tvDuration: TextView = itemView.findViewById<View>(R.id.tv_duration) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active) as ImageView

        fun bind(song: Song, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(song, layoutPosition) }
        }

    }

    interface RecyclerItemClickListener {
        fun onClickListener(song: Song, position: Int)
    }
}
