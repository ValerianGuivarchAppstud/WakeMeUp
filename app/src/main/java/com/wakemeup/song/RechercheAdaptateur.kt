package com.wakemeup.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R

class RechercheAdaptateur(
    val recherhes: MutableList<String>
    , val listener: RechercheAdaptateur.RecyclerItemClickListener
) : RecyclerView.Adapter<RechercheAdaptateur.ViewHolder>(){

    interface RecyclerItemClickListener {
        fun onClickListener(recherche : String, position: Int)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cardView = itemView.findViewById<TextView>(R.id.cardView_history) as CardView
        val recherche = itemView.findViewById<TextView>(R.id.recherche)

        fun bind(recherche : String, listener: RechercheAdaptateur.RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(recherche, layoutPosition) }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RechercheAdaptateur.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_history, parent, false)
        return ViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return recherhes.size
    }

    override fun onBindViewHolder(holder: RechercheAdaptateur.ViewHolder, position: Int) {
        val recherche = recherhes[position]
        holder.recherche.text= recherche
        holder.cardView.tag = position
        holder.bind(recherche, listener)
    }


}