package com.vguivarc.wakemeup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.data.repository.Repository
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.ui.MainActivity

class CurrentUserViewModel(val repo: Repository) : ViewModel() {

    private val currentUserState = MediatorLiveData<UserModel?>()
    fun getCurrentUserLiveData(): LiveData<UserModel?> = currentUserState

    init {
        currentUserState.addSource(repo.getCurentUserLiveData()) { newCurrentUser ->
            currentUserState.value = newCurrentUser
        }
    }

    fun addFavoriString(mainActivity: MainActivity, favyt: String) {
        repo.addFavoriString(mainActivity, favyt)
    }
}
