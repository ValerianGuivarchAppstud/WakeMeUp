package com.vguivarc.wakemeup.transport.favoritelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Favorite

class FavoriteListAdapter(
    private var favoriteList: List<Favorite>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<FavoriteListAdapter.FavorisViewHolder>() {
    var selectedSong: Favorite? = null


    fun updateData(favoriteList: List<Favorite>) {
        this.favoriteList = favoriteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavorisViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_favori, parent, false)
        return FavorisViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(
        holder: FavorisViewHolder,
        position: Int
    ) {
        val song = favoriteList[position].song
        holder.tvTitle.text = song.title
        val favoriteSong = favoriteList[position]
        if (selectedSong?.song?.id == favoriteSong.song.id) {
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

        Glide.with(holder.ivArtwork)
            .load(song.pictureUrl)
            .into(holder.ivArtwork)
//        Picasso.get().load().placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(favoriteList[position], listener)
    }

    interface RecyclerItemClickListener {
        fun onPlayListener(recherche: Favorite, position: Int)
        fun onShareListener(recherche: Favorite, position: Int)
        fun onDeleteListener(recherche: Favorite, position: Int)
    }

    class FavorisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val ivArtwork: ImageView = itemView.findViewById(R.id.iv_artwork)
        private val ivPlayActive: ImageView = itemView.findViewById(R.id.iv_play_active)
        private val ivShare: ImageView = itemView.findViewById(R.id.song_item_share)
        private val ivDelete: ImageView = itemView.findViewById(R.id.song_item_remove_favorite)

        fun bind(song: Favorite, listener: RecyclerItemClickListener) {
            ivPlayActive.setOnClickListener { listener.onPlayListener(song, layoutPosition) }
            ivShare.setOnClickListener { listener.onShareListener(song, layoutPosition) }
            ivDelete.setOnClickListener { listener.onDeleteListener(song, layoutPosition) }
        }
    }

    fun selectedSong(s: Favorite) {
        selectedSong = s
        notifyDataSetChanged()
    }
}
