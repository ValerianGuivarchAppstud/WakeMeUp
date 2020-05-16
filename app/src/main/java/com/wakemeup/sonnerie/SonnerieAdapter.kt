package com.wakemeup.sonnerie

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R

class SonnerieAdapter (private val context: Context,
                       private val sonnerieList: MutableList<SonnerieRecue>,
                       private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<SonnerieAdapter.SonnerieViewHolder>() {

    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonnerieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_sonnerie_attente, parent, false)
        return SonnerieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SonnerieViewHolder, position: Int) {

        val sonnerie = sonnerieList.get(position)
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

        holder.sender.text = "From : ${sonnerie.senderName}"
        holder.bind(sonnerie, listener)

    }

    override fun getItemCount(): Int {
        return sonnerieList.size
    }

    class SonnerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sender: TextView = itemView.findViewById<View>(R.id.nom_ami_sonnerie_attente) as TextView

        fun bind(sonnerie: SonnerieRecue, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickSonnerieListener(sonnerie, layoutPosition) }
        }

    }

    interface RecyclerItemClickListener {
        fun onClickSonnerieListener(sonnerie: SonnerieRecue, position: Int)
    }
}