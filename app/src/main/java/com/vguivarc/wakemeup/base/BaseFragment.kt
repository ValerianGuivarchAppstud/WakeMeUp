package com.vguivarc.wakemeup.base

import androidx.fragment.app.Fragment
import org.koin.core.component.KoinComponent

open class BaseFragment(layoutId: Int = 0) : Fragment(layoutId), KoinComponent
