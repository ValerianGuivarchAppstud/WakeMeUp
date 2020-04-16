package com.wakemeup.reveil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R

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
        val reveil_card_view = itemView.findViewById<CardView>(R.id.reveil_card_view)!!
        var item_reveil_heure = itemView.findViewById<TextView>(R.id.item_reveil_heure)!!
        var item_reveil_jours = itemView.findViewById<TextView>(R.id.item_reveil_jours)!!
        var item_reveil_switch = itemView.findViewById<Switch>(R.id.item_reveil_switch)!!
        var item_reveil_delete = itemView.findViewById<ImageButton>(R.id.item_reveil_delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_reveil, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reveil = reveils.toList()[position].second
        with(holder) {
            reveil_card_view.tag = reveil
            reveil_card_view.setOnClickListener(this@ReveilListeAdapter)
            item_reveil_heure.text = reveil.getHeureTexte()
            item_reveil_jours.text = reveil.getJoursTexte()
            item_reveil_switch.isChecked = reveil.isActif
            item_reveil_switch.setOnClickListener {
                listener?.onReveilSwitched(reveil)
            }
            item_reveil_delete.setOnClickListener {
                listener?.onReveilDelete(reveil)
            }
        }
    }

    override fun getItemCount(): Int = reveils.size

    override fun onClick(v: View) {
        listener?.onReveilClicked(v.tag as ReveilModel, v)
    }
}