package com.wakemeup.song

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wakemeup.R
import com.wakemeup.util.Utility

class HistVideoAdaptater(
    private val context: Context,
    private val historiqueVideo: MutableList<Song>,
    private val listener: HistVideoAdaptater.RecyclerItemClickListener
) : RecyclerView.Adapter<HistVideoAdaptater.HistVideoViewHolder>(){
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistVideoAdaptater.HistVideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_video, parent, false)
        return HistVideoViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return historiqueVideo.size
    }

    override fun onBindViewHolder(holder: HistVideoAdaptater.HistVideoViewHolder, position: Int) {
        val song = historiqueVideo[position]
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


    interface RecyclerItemClickListener {
        fun onClickListener(recherche : Song, position: Int)
    }

    class HistVideoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val tvArtist: TextView = itemView.findViewById<View>(R.id.tv_artist) as TextView
        val tvDuration: TextView = itemView.findViewById<View>(R.id.tv_duration) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active) as ImageView

        fun bind(song: Song, listener: HistVideoAdaptater.RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(song, layoutPosition) }
        }

    }

}