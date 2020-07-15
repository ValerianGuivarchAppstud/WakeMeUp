package com.vguivarc.wakemeup.sonnerie

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import java.text.SimpleDateFormat
import java.util.*

class SonnerieAttenteAdapter(
    private val sonnerieList: MutableList<Sonnerie>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonnerieAttenteAdapter.SonnerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_sonnerie_attente, parent, false)
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = sonnerieList.get(position)
        //todo translate
        if(sonnerie.sender!=null){
            holder.sender.text = "Envoyé par : ${sonnerie.sender!!.username}"
        } else {
            holder.sender.text = "Envoyé par : ${sonnerie.senderName}"
        }

        val formatterDay = SimpleDateFormat("dd/MM/yyyy")
        val formatterHour = SimpleDateFormat("hh:mm")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = sonnerie.dateEnvoie*1000

        holder.date.text = "Reçue le ${formatterDay.format(calendar.time)} à ${formatterHour.format(calendar.time)}"
        if(sonnerie.sender==null || sonnerie.sender!!.imageUrl==""){
            holder.image.setImageDrawable(ContextCompat.getDrawable(AppWakeUp.appContext, R.drawable.empty_picture_profil))
        } else {
            Picasso.get().load(sonnerie.sender!!.imageUrl).placeholder(R.drawable.music_placeholder)
                .into(holder.image)
        }
        holder.bind(sonnerie, listener)

    }

    override fun getItemCount(): Int {
        return sonnerieList.size
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender = itemView.findViewById<TextView>(R.id.nom_ami_sonnerie_attente)
        val date = itemView.findViewById<TextView>(R.id.date_sonnerie_attente)
        val image = itemView.findViewById<ImageView>(R.id.image_ami_sonnerie_attente)

        fun bind(sonnerie: Sonnerie, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickSonnerieListener(sonnerie, layoutPosition) }
        }

    }

    interface RecyclerItemClickListener {
        fun onClickSonnerieListener(sonnerie: Sonnerie, position: Int)
    }
}