package com.wakemeup.amis.attente

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R
import com.wakemeup.amis.SonnerieEnAttente
import com.wakemeup.connect.UserModel


class SonnerieAttenteAdapter(
    private val context: Context,
    private val listeSonneriesEnAttente: MutableList<SonnerieEnAttente>,
    private val listener: View.OnClickListener
) : RecyclerView.Adapter<SonnerieAttenteAdapter.SonnerieAttenteViewHolder>() {

    class SonnerieAttenteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val item_ami_nom: TextView =
            itemView.findViewById<View>(R.id.nom_ami_sonnerie_attente) as TextView
        val cardView: CardView =
            itemView.findViewById<Switch>(R.id.card_view_item_sonnerie_attente) as CardView

        fun bind(ami: UserModel, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(ami, layoutPosition) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieAttenteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_sonnerie_attente, parent, false)
        return SonnerieAttenteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SonnerieAttenteViewHolder, position: Int) {

        val sonnerie = listeSonneriesEnAttente[position]
        holder.item_ami_nom.text = sonnerie.sender
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return listeSonneriesEnAttente.size
    }


    interface RecyclerItemClickListener {
        fun onClickListener(ami: UserModel, position: Int)
    }
}
