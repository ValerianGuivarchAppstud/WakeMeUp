package com.vguivarc.wakemeup.sonnerie

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
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.song.favori.Favori
import com.vguivarc.wakemeup.song.favori.FavorisAdaptater
import org.w3c.dom.Text

class SonneriePasseAdapter (private val context: Context,
                            private val sonnerieList: MutableList<Sonnerie>,
                            private val favoriList: MutableList<Favori>,
                            private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonneriePasseAdapter.SonnerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_sonnerie_passe, parent, false)
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = sonnerieList.get(position)
        if(sonnerie.sender!=null){
            holder.sender.text = "Envoyé par : "
            holder.link.visibility = View.VISIBLE
            holder.link.text="    "+sonnerie.sender!!.username+"    "

        } else {
            holder.sender.text = "Envoyé par : ${sonnerie.senderName}"
            holder.link.visibility = View.GONE
        }
        val song = sonnerieList[position].song!!
        holder.tvTitle.text = song.title
        //holder.tvArtist.text = song.artist
        if(favoriList.filter { fav -> fav.idSong==sonnerie.idSong }.size==0){
            holder.ivFavori.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fav_not_yet))
        } else {
            holder.ivFavori.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_main_menu_favoris))
        }
        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder).into(holder.ivArtwork)
        holder.bind(sonnerie, sonnerie.sender, listener)

    }

    override fun getItemCount(): Int {
        return sonnerieList.size
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender: TextView =
            itemView.findViewById<View>(R.id.nom_ami_sonnerie_passe) as TextView

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title_sonnerie_passe) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork_sonnerie_passe) as ImageView
        val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active_sonnerie_passe) as ImageView
        val ivShare: ImageView = itemView.findViewById<View>(R.id.pas_tv_share) as ImageView
        val ivDelete: ImageView = itemView.findViewById<View>(R.id.pas_tv_delete) as ImageView
        val ivFavori: ImageView = itemView.findViewById<View>(R.id.pas_tv_fav) as ImageView
        val link: TextView = itemView.findViewById(R.id.nom_ami_sonnerie_passe_link)

        fun bind(song: Sonnerie, user: UserModel?, listener: RecyclerItemClickListener) {
            ivPlayActive.setOnClickListener { listener.onPlayListener(song, layoutPosition) }
            ivShare.setOnClickListener { listener.onShareListener(song, layoutPosition) }
            ivDelete.setOnClickListener { listener.onDeleteListener(song, layoutPosition) }
            ivFavori.setOnClickListener { listener.onFavoriListener(song, layoutPosition) }
            link.setOnClickListener { listener.onNameListener(user, layoutPosition) }
        }
    }


    interface RecyclerItemClickListener {
        fun onPlayListener(sonnerie: Sonnerie, position: Int)
        fun onShareListener(sonnerie: Sonnerie, position: Int)
        fun onDeleteListener(sonnerie: Sonnerie, position: Int)
        fun onFavoriListener(sonnerie: Sonnerie, position: Int)
        fun onNameListener(user: UserModel?, position: Int)


    }

}