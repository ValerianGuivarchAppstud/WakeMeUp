package com.vguivarc.wakemeup.transport.ringinglist

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.FavoriteInteractor
import com.vguivarc.wakemeup.domain.external.RingingInteractor
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class RingingListViewModel(
    private val ringinInteractor: RingingInteractor
) : ViewModel() {

    }
