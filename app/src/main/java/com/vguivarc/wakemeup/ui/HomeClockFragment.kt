package com.vguivarc.wakemeup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseFragment

class HomeClockFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_clock, container, false)
    }
}