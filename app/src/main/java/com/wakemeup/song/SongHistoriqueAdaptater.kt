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




//------------------------------------------------------------------------------------------//
//CET ADAPTATER EST POUR LES MUSIQUES DANS L'HISTORIQUE DES VIDEOS ET AUSSI POUR LES FAVORIS//
//------------------------------------------------------------------------------------------//

class SongHistoriqueAdaptater(
    private val context: Context,
    private val laClass : String,
    private val historiqueVideo: MutableList<SongHistorique>,
    private val listener: SongHistoriqueAdaptater.RecyclerItemClickListener
) : RecyclerView.Adapter<SongHistoriqueAdaptater.SongHistoriqueViewHolder>() {
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongHistoriqueAdaptater.SongHistoriqueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_video, parent, false)
        return SongHistoriqueViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return historiqueVideo.size
    }

    override fun onBindViewHolder(
        holder: SongHistoriqueViewHolder,
        position: Int
    ) {
        val song = historiqueVideo[position].song

        //Si l'adaptateur est celui des favoris, on met un fond bleu quand on clic sur une musique
        if (laClass == HISTORIQUE){
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.transparent
                    )
                )

            } else {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
        }
        //----------------------------------------------------------------------------------------

        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist
        val duration = Utility.convertDuration(song.duration)

        holder.tvDuration.text = duration

        Picasso.get().load(song.artworkUrl).placeholder(R.drawable.music_placeholder)
            .into(holder.ivArtwork)
        holder.bind(historiqueVideo[position], listener)
    }


    interface RecyclerItemClickListener {
        fun onClickListener(recherche: SongHistorique, position: Int)
    }

    class SongHistoriqueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val tvArtist: TextView = itemView.findViewById<View>(R.id.tv_artist) as TextView
        val tvDuration: TextView = itemView.findViewById<View>(R.id.tv_duration) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active) as ImageView

        fun bind(song: SongHistorique, listener: SongHistoriqueAdaptater.RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(song, layoutPosition) }
        }
    }

    companion object {
        private const val HISTORIQUE = "HISTORIQUE"
    }
}
