package com.wakemeup.amis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R
import com.wakemeup.connect.UserModel


class AmiAdapter(
    private val context: Context,
    private val listeAmis: MutableList<UserModel>,
    private val listeSelection: MutableList<Int>,
    private val listener: View.OnClickListener
) : RecyclerView.Adapter<AmiAdapter.AmiViewHolder>() {

    class AmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val item_ami_nom: TextView = itemView.findViewById<View>(R.id.nom_ami) as TextView
        val cardView: CardView = itemView.findViewById<Switch>(R.id.card_view_item_ami) as CardView
        val list_ami_valider: ImageView =
            itemView.findViewById<Switch>(R.id.list_ami_valider) as ImageView

        fun bind(ami: UserModel, listener: RecyclerItemClickListener) {
            itemView.setOnClickListener { listener.onClickListener(ami, layoutPosition) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmiViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_ami, parent, false)
        return AmiViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmiViewHolder, position: Int) {

        val ami = listeAmis[position]
        if (listeSelection.contains(position)) {
            holder.list_ami_valider.setBackgroundResource(R.drawable.selection_oui)
            holder.cardView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
        } else {
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
            holder.list_ami_valider.setBackgroundResource(R.drawable.selection_non)
        }
        holder.item_ami_nom.text = ami.username
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return listeAmis.size
    }


    interface RecyclerItemClickListener {
        fun onClickListener(ami: UserModel, position: Int)
    }
}
