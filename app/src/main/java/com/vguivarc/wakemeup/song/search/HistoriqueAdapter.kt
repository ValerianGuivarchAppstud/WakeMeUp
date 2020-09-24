package com.vguivarc.wakemeup.song.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R


class HistoriqueAdapter(
    private val recherhes: MutableList<String>
    , val listener: RechercheVideoAdapterListener
) : RecyclerView.Adapter<HistoriqueAdapter.ViewHolder>(),
    View.OnClickListener {

    interface RechercheVideoAdapterListener {
        fun onHistoriqueClicked(recherche: String, itemView: View)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val cardView : CardView = itemView.findViewById(R.id.cardView_history)
        val recherche: TextView = itemView.findViewById(R.id.recherche)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_list_history, parent, false)
        return ViewHolder((viewItem))
    }

    override fun getItemCount(): Int {
        return recherhes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recherche = recherhes[position]
        holder.recherche.text= recherche
        holder.cardView.tag = recherche
    }


    override fun onClick(v: View) {
        listener.onHistoriqueClicked(v.tag as String, v)
    }
}