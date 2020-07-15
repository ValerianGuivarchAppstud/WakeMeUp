package com.vguivarc.wakemeup.song.favori

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
import com.vguivarc.wakemeup.util.Utility


//----------------------------------------------------------------//
//CET ADAPTATER EST POUR LES FAVORIS//
//----------------------------------------------------------------//

class FavorisAdaptater(
    private val context: Context,
    private val favorisVideo: MutableMap<String, Favori>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<FavorisAdaptater.FavorisViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavorisViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_favori, parent, false)
        return FavorisViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return favorisVideo.size
    }

    override fun onBindViewHolder(
        holder: FavorisViewHolder,
        position: Int
    ) {
        val song = favorisVideo.values.toList()[position].song!!
        holder.tvTitle.text = song.title

        //TODO remplacer par l'autre, glide ?
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(favorisVideo.values.toList()[position], listener)
    }


    interface RecyclerItemClickListener {
        fun onPlayListener(recherche: Favori, position: Int)
        fun onShareListener(recherche: Favori, position: Int)
        fun onDeleteListener(recherche: Favori, position: Int)
    }

    class FavorisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.fav_tv_title) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.fav_iv_artwork) as ImageView
        val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.fav_iv_play_active) as ImageView
        val ivShare: ImageView = itemView.findViewById<View>(R.id.fav_tv_share) as ImageView
        val ivDelete: ImageView = itemView.findViewById<View>(R.id.fav_tv_delete) as ImageView

        fun bind(song: Favori, listener: RecyclerItemClickListener) {
            ivPlayActive.setOnClickListener { listener.onPlayListener(song, layoutPosition) }
            ivShare.setOnClickListener { listener.onShareListener(song, layoutPosition) }
            ivDelete.setOnClickListener { listener.onDeleteListener(song, layoutPosition) }
        }
    }


}
