package com.vguivarc.wakemeup.ui.favoritelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.Favorite

class FavoriteAdapter(
    private var favoriteList: List<Favorite>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<FavoriteAdapter.FavorisViewHolder>() {

    fun setFavoriteList(favoriteList: List<Favorite>) {
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
        val song = favoriteList[position].song!!
        holder.tvTitle.text = song.title

        // TODO remplacer par l'autre, glide ?
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(favoriteList[position], listener)
    }

    interface RecyclerItemClickListener {
        fun onPlayListener(recherche: Favorite, position: Int)
        fun onShareListener(recherche: Favorite, position: Int)
        fun onDeleteListener(recherche: Favorite, position: Int)
    }

    class FavorisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.fav_tv_title) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.fav_iv_artwork) as ImageView
        private val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.fav_iv_play_active) as ImageView
        private val ivShare: ImageView = itemView.findViewById<View>(R.id.fav_tv_share) as ImageView
        private val ivDelete: ImageView = itemView.findViewById<View>(R.id.fav_tv_delete) as ImageView

        fun bind(song: Favorite, listener: RecyclerItemClickListener) {
            ivPlayActive.setOnClickListener { listener.onPlayListener(song, layoutPosition) }
            ivShare.setOnClickListener { listener.onShareListener(song, layoutPosition) }
            ivDelete.setOnClickListener { listener.onDeleteListener(song, layoutPosition) }
        }
    }
}
