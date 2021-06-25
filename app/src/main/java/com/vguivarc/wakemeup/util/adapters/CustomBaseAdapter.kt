package com.vguivarc.wakemeup.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * All adapters can now extend BaseAdapter. It helps building adapters quicker and evenly.
 * The new adapter must at least set the variables mData (with the type you wish) and onBinding.
 *
 * If you desire an adapter with header or footer, you should instead extend HeaderFooterAdapter.
 */
open class CustomBaseAdapter<T>(
    protected var mData: List<T>,
    private var itemLayoutId: Int,
    private val onClickListener: ((T) -> Unit)? = null
) :
    RecyclerView.Adapter<CustomBaseAdapter.SuperViewHolder<T>>() {

    protected var onBinding: (view: View, item: T) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder<T> {
        return ItemViewHolder(
            parent.inflate(itemLayoutId),
            onBinding
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: SuperViewHolder<T>, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindView(mData[position], onClickListener)
        }
    }

    open class SuperViewHolder<T>(parent: View) : RecyclerView.ViewHolder(parent)

    class HeaderViewHolder<T>(parent: View) : SuperViewHolder<T>(parent)

    class FooterViewHolder<T>(parent: View) : SuperViewHolder<T>(parent)

    class ItemViewHolder<T>(parent: View, private val onBinding: (view: View, item: T) -> Unit) :
        SuperViewHolder<T>(parent) {

        fun bindView(item: T, onClickListener: ((T) -> Unit)? = null) {
            onBinding.invoke(itemView, item)
            itemView.setOnClickListener { onClickListener?.invoke(item) }
        }
    }
}
