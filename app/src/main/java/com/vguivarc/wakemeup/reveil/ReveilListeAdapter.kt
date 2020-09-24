package com.vguivarc.wakemeup.reveil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R

class ReveilListeAdapter(
    private val reveils: Map<Int, ReveilModel>,
    private val listener: ReveilListAdapterListener?
) : RecyclerView.Adapter<ReveilListeAdapter.ViewHolder>(),
    View.OnClickListener {

    interface ReveilListAdapterListener {
        fun onReveilClicked(reveilModel: ReveilModel, itemView: View)
        fun onReveilSwitched(reveilModel: ReveilModel)
        fun onReveilDelete(reveilModel: ReveilModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reveilCardView = itemView.findViewById<CardView>(R.id.reveil_card_view)!!
        var itemReveilHeure = itemView.findViewById<TextView>(R.id.item_reveil_heure)!!
        var itemReveilJours = itemView.findViewById<TextView>(R.id.item_reveil_jours)!!
        var itemReveilSwitch = itemView.findViewById<SwitchCompat>(R.id.item_reveil_switch)!!
        var itemReveilDelete = itemView.findViewById<ImageButton>(R.id.fav_tv_delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_reveil, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reveil = reveils.toList()[position].second
        with(holder) {
            reveilCardView.tag = reveil
            reveilCardView.setOnClickListener(this@ReveilListeAdapter)
            itemReveilHeure.text = reveil.getHeureTexte()
            itemReveilJours.text = reveil.getJoursTexte()
            itemReveilSwitch.isChecked = reveil.isActif
            itemReveilSwitch.setOnClickListener {
                listener?.onReveilSwitched(reveil)
            }
            itemReveilDelete.setOnClickListener {
                listener?.onReveilDelete(reveil)
            }
        }
    }

    override fun getItemCount(): Int = reveils.size

    override fun onClick(v: View) {
        listener?.onReveilClicked(v.tag as ReveilModel, v)
    }
}