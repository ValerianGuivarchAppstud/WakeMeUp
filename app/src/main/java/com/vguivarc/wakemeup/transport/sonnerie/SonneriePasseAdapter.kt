package com.vguivarc.wakemeup.transport.sonnerie

import android.annotation.SuppressLint
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
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

class SonneriePasseAdapter(
    private val context: Context,
    private val ringingList: MutableList<Ringing>,
    private val favoriList: MutableList<Favorite>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonneriePasseAdapter.SonnerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_sonnerie_passe, parent, false)
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = ringingList[position]
        if (sonnerie.sender != null) {
            holder.sender.text = "Envoyé par : "
            holder.link.visibility = View.VISIBLE
            holder.link.text = "    " + sonnerie.sender!!.username + "    "
        } else {
            holder.sender.text = "Envoyé par : ${sonnerie.senderName}"
            holder.link.visibility = View.GONE
        }
        val song = ringingList[position].song!!
        holder.tvTitle.text = song.title
        // holder.tvArtist.text = song.artist
        if (favoriList.filter { fav -> fav.song.id == sonnerie.song!!.id }.isEmpty()) {
            holder.ivFavori.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_yes))
        } else {
            holder.ivFavori.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_main_menu_favoris))
        }
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(sonnerie, sonnerie.sender, listener)
    }

    override fun getItemCount(): Int {
        return ringingList.size
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender: TextView =
            itemView.findViewById<View>(R.id.nom_ami_sonnerie_passe) as TextView

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title_sonnerie_passe) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork_sonnerie_passe) as ImageView
        private val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active_sonnerie_passe) as ImageView
        private val ivShare: ImageView = itemView.findViewById<View>(R.id.pas_tv_share) as ImageView
        private val ivDelete: ImageView = itemView.findViewById<View>(R.id.pas_tv_delete) as ImageView
        val ivFavori: ImageView = itemView.findViewById<View>(R.id.pas_tv_fav) as ImageView
        val link: TextView = itemView.findViewById(R.id.nom_ami_sonnerie_passe_link)

        fun bind(song: Ringing, user: UserProfile?, listener: RecyclerItemClickListener) {
            ivPlayActive.setOnClickListener { listener.onPlayListener(song, layoutPosition) }
            ivShare.setOnClickListener { listener.onShareListener(song, layoutPosition) }
            ivDelete.setOnClickListener { listener.onDeleteListener(song, layoutPosition) }
            ivFavori.setOnClickListener { listener.onFavoriListener(song, layoutPosition) }
            link.setOnClickListener { listener.onNameListener(user, layoutPosition) }
        }
    }

    interface RecyclerItemClickListener {
        fun onPlayListener(ringing: Ringing, position: Int)
        fun onShareListener(ringing: Ringing, position: Int)
        fun onDeleteListener(ringing: Ringing, position: Int)
        fun onFavoriListener(ringing: Ringing, position: Int)
        fun onNameListener(user: UserProfile?, position: Int)
    }
}
