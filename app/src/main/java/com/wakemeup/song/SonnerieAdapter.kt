package com.wakemeup.song

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
import com.wakemeup.R
import com.wakemeup.contact.SonnerieRecue
import com.wakemeup.util.Utility

class SonnerieAdapter (private val context: Context,
private val sonnerieMap: MutableMap<String, SonnerieRecue>,
private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonnerieAdapter.SonnerieViewHolder>() {

    var selectedPosition: Int = 0
    private val sonnerieList: MutableList<SonnerieRecue>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_video, parent, false)
        convertMapToListSortDate()
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = sonnerieList?.get(position)
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

        if (sonnerie != null) {
            holder.sender.text = "From : ${sonnerie.senderName}"
            val duration = Utility.convertDuration(sonnerie.song.duration)

            holder.tvDuration.text = duration

            Picasso.get().load(sonnerie.song.artworkUrl).placeholder(R.drawable.music_placeholder)
                .into(holder.ivArtwork)
            holder.bind(sonnerie, listener)
        }

    }

    override fun getItemCount(): Int {
        if (sonnerieList != null) return sonnerieList.size
        return 0
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender: TextView = itemView.findViewById<View>(R.id.nom_ami_sonnerie_attente) as TextView
        //val tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        //val tvArtist: TextView = itemView.findViewById<View>(R.id.tv_artist) as TextView
        val tvDuration: TextView = itemView.findViewById<View>(R.id.tv_duration) as TextView
        val ivArtwork: ImageView = itemView.findViewById<View>(R.id.iv_artwork) as ImageView
        //val ivPlayActive: ImageView = itemView.findViewById<View>(R.id.iv_play_active) as ImageView

        fun bind(sonnerie: SonnerieRecue, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickSonnerieListener(sonnerie, layoutPosition) }
        }

    }

    private fun convertMapToListSortDate() {
        sonnerieList?.addAll(sonnerieMap.values)
        //todo tri par date
        sonnerieList?.sortWith(compareBy { v -> v.sonnerieId })
    }

    interface RecyclerItemClickListener {
        fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int)
    }
}