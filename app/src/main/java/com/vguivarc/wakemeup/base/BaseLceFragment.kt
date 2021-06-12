package com.vguivarc.wakemeup.base

import android.os.Bundle
import android.view.View
import com.vguivarc.wakemeup.R
import org.jetbrains.anko.findOptional

abstract class BaseLceFragment(layoutId: Int = 0) : BaseFragment(layoutId) {
    private var loadingView: View? = null
    private var contentView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingView = view?.findOptional(R.id.loadingView) as? View
        contentView = view?.findOptional(R.id.contentView) as? View
        errorView = view?.findOptional(R.id.errorView) as? View
        emptyView = view?.findOptional(R.id.emptyView) as? View
        errorView?.setOnClickListener { retry() }
    }

    fun showLoading() {
        contentView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
    }

    open fun showError(throwable: Throwable? = null) {
        errorView?.visibility = View.VISIBLE
        contentView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
    }

    fun showContent() {
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        contentView?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
    }

    fun showEmptyView() {
        errorView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        contentView?.visibility = View.VISIBLE
        emptyView?.visibility = View.VISIBLE
    }

    open fun retry() {
        // override only when a retry behaviour is relevant
    }
}
