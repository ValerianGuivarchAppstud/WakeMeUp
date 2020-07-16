package com.vguivarc.wakemeup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.repo.Repository

class CurrentUserViewModel(val repo: Repository) : ViewModel() {

    private val currentUserState = MediatorLiveData<FirebaseUser?>()
    fun getCurrentUserLiveData(): LiveData<FirebaseUser?> = currentUserState

    init {
        currentUserState.addSource(repo.getCurentUserLiveData()) { newCurrentUser ->
            currentUserState.value = newCurrentUser
        }
    }


    fun addFavoriString(mainActivity: MainActivity, favyt: String) {
        repo.addFavoriString(mainActivity, favyt)
    }

    fun disconnect() {
        repo.disconnect()
    }

}