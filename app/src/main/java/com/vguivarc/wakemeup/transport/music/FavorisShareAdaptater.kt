package com.vguivarc.wakemeup.transport.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Favorite

class FavorisShareAdaptater(
    private val favorisVideo: MutableMap<String, Favorite>,
    private val selection: MutableList<Favorite>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<FavorisShareAdaptater.FavorisViewHolder>() {

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
        val song = favorisVideo.toList()[position].second.song
        holder.tvTitle.text = song.title

        // TODO remplacer par l'autre, glide ?
        Picasso.get().load(song.pictureUrl).placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(favorisVideo.values.toList()[position], listener)

        holder.ivPlayActive.visibility = View.GONE
        holder.ivShare.visibility = View.GONE
        holder.ivDelete.visibility = View.GONE
    }

    interface RecyclerItemClickListener {
        fun onSelect(recherche: Favorite, position: Int)
    }

    class FavorisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val ivArtwork: ImageView = itemView.findViewById(R.id.iv_artwork)
        val ivPlayActive: ImageView =
            itemView.findViewById(R.id.iv_play_active)
        val ivShare: ImageView = itemView.findViewById(R.id.song_item_share)
        val ivDelete: ImageView = itemView.findViewById(R.id.song_item_remove_favorite)

        fun bind(song: Favorite, listener: RecyclerItemClickListener) {

        }
    }
}
