package com.dupuis.webtoonfactory.util.adapters

import android.view.View
import android.view.ViewGroup
import com.vguivarc.wakemeup.util.adapters.CustomBaseAdapter

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1
private const val TYPE_FOOTER = 2

/**
 * If you need to add a header or/and a footer to your recycler view, let your adapter extends this HeaderFooterAdapter
 * class. You just have to pass the view (or the layout ID) into the constructor of your adapter.
 */
open class HeaderFooterAdapter<T> :
    CustomBaseAdapter<T> {

    private var itemLayoutId: Int
    private var headerView: View?
    private var headerLayoutId: Int?
    private var footerView: View?
    private var footerLayoutId: Int?

    private var hasAHeader = false
    private var hasAFooter = false

    /**
     * Constructor using a view for header and footer
     * @param data list of items in BaseAdapter
     * @param itemLayoutId layout of one item in BaseAdapter
     * @param headerView optional view created dynamically
     * @param footerView optional view created dynamically
     */
    constructor(
        data: List<T>,
        itemLayoutId: Int,
        headerView: View? = null,
        footerView: View? = null,
        onClickListener: ((T) -> Unit)? = null
    ) : super(
        data,
        itemLayoutId,
        onClickListener = onClickListener
    ) {
        this.itemLayoutId = itemLayoutId
        this.headerView = headerView
        this.footerView = footerView
        this.headerLayoutId = null
        this.footerLayoutId = null
        headerView?.let { hasAHeader = true }
        footerView?.let { hasAFooter = true }
    }

    /**
     * Constructor using a layout ID for header and footer
     * @param data list of items in BaseAdapter
     * @param itemLayoutId layout of one item in BaseAdapter
     * @param headerLayoutId optional layout ID (R.layout.header_layout_example)
     * @param footerLayoutId optional layout ID (R.layout.footer_layout_example)
     */
    constructor(
        data: List<T>,
        itemLayoutId: Int,
        headerLayoutId: Int? = null,
        footerLayoutId: Int? = null,
        onClickListener: ((T) -> Unit)? = null
    ) : super(
        data, itemLayoutId, onClickListener = onClickListener
    ) {
        this.itemLayoutId = itemLayoutId
        this.headerLayoutId = headerLayoutId
        this.footerLayoutId = footerLayoutId
        this.headerView = null
        this.footerView = null
        headerLayoutId?.let { hasAHeader = true }
        footerLayoutId?.let { hasAFooter = true }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomBaseAdapter.SuperViewHolder<T> {
        return when (viewType) {
            TYPE_HEADER -> {
                CustomBaseAdapter.HeaderViewHolder(headerView ?: parent.inflate(headerLayoutId!!))
            }
            TYPE_FOOTER -> CustomBaseAdapter.FooterViewHolder(
                footerView ?: parent.inflate(
                    footerLayoutId!!
                )
            )
            else -> CustomBaseAdapter.ItemViewHolder(parent.inflate(itemLayoutId), onBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType: Int? = null
        if (hasAHeader && position == 0) {
            // We have a list with a header
            viewType = TYPE_HEADER
        } else if (hasAFooter && !hasAHeader && position == mData.size) {
            // We have a list with just a footer
            viewType = TYPE_FOOTER
        } else if (hasAFooter && hasAHeader && position == (mData.size + 1)) {
            // We have a list with a header and a footer
            viewType = TYPE_FOOTER
        }

        // If viewType is still null, we just have a list
        return viewType ?: TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return mData.size + getExtraCell()
    }

    override fun onBindViewHolder(holder: SuperViewHolder<T>, position: Int) {
        // Correct the position if we have a header
        if (holder is ItemViewHolder) {
            if (hasAHeader) {
                super.onBindViewHolder(holder, position - 1)
            } else {
                super.onBindViewHolder(holder, position)
            }
        }
    }

    private fun getExtraCell(): Int {
        var extraCell = 0
        if (hasAHeader) {
            extraCell++
        }
        if (hasAFooter) {
            extraCell++
        }
        return extraCell
    }
}
