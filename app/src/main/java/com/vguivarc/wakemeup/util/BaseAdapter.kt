package com.vguivarc.wakemeup.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * All adapters can now extend BaseAdapter. It helps building adapters quicker and evenly.
 * The new adapter must at least set the variables mData (with the type you wish) and onBinding.
 *
 * If you desire an adapter with header or footer, you should instead extend SuperAdapter.
 */
open class BaseAdapter<T>(
    protected var mData: List<T>,
    private var itemLayoutId: Int,
    private val onClickListener: ((T) -> Unit)? = null
) :
    RecyclerView.Adapter<BaseAdapter.SuperViewHolder<T>>() {

    fun updateData(newList: List<T>) {
        mData = newList
        notifyDataSetChanged()
    }

    protected var onBinding: (view: View, item: T, position: Int?) -> Unit = { _, _, _ -> Unit }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        return ItemViewHolder(view, onBinding)
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

    class ItemViewHolder<T>(
        parent: View,
        private val onBinding: (view: View, item: T, position: Int?) -> Unit
    ) :
        SuperViewHolder<T>(parent) {

        fun bindView(item: T, onClickListener: ((T) -> Unit)? = null) {
            onBinding.invoke(itemView, item, adapterPosition)
            onClickListener?.let { listener -> itemView.setOnClickListener { listener.invoke(item) } }
        }
    }
}
