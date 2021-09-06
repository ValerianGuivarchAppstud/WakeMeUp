package com.vguivarc.wakemeup.transport.sonnerie

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import java.text.SimpleDateFormat
import java.util.*

class SonnerieAttenteAdapter(
    private val ringingList: MutableList<Ringing>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonnerieAttenteAdapter.SonnerieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_sonnerie_attente, parent, false)
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = ringingList[position]
        // todo translate
        if (sonnerie.sender != null) {
            holder.sender.text = "Envoyé par : ${sonnerie.sender!!.username}"
        } else {
            holder.sender.text = "Envoyé par : ${sonnerie.senderName}"
        }

        val formatterDay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatterHour = SimpleDateFormat("hh:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = sonnerie.dateSent * 1000

        holder.date.text = "Reçue le ${formatterDay.format(calendar.time)} à ${formatterHour.format(calendar.time)}"
        if (sonnerie.sender == null || sonnerie.sender!!.imageUrl == "") {
            holder.image.setImageDrawable(ContextCompat.getDrawable(AndroidApplication.appContext, R.drawable.empty_picture_profil))
        } else {
            Picasso.get().load(sonnerie.sender!!.imageUrl).placeholder(R.drawable.music_placeholder)
                .into(holder.image)
        }
        holder.bind(sonnerie, listener)
    }

    override fun getItemCount(): Int {
        return ringingList.size
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender: TextView = itemView.findViewById(R.id.nom_ami_sonnerie_attente)
        val date: TextView = itemView.findViewById(R.id.date_sonnerie_attente)
        val image: ImageView = itemView.findViewById(R.id.image_ami_sonnerie_attente)

        fun bind(ringing: Ringing, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickSonnerieListener(ringing, layoutPosition) }
        }
    }

    interface RecyclerItemClickListener {
        fun onClickSonnerieListener(ringing: Ringing, position: Int)
    }
}
