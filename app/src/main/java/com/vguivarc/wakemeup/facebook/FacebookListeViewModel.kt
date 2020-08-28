package com.vguivarc.wakemeup.facebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.repo.Repository

class FacebookListeViewModel(val repo: Repository) : ViewModel(){

    private val friendListeState = MediatorLiveData<FacebookResult>()
    fun getFriendsListeLiveData(): LiveData<FacebookResult> = friendListeState

    init {
        friendListeState.addSource(repo.getFacebookListLiveData()) { newFriends ->
            friendListeState.value = newFriends
        }
    }

    fun getFriends() {
        repo.requestFacebookFriendData()
    }
}