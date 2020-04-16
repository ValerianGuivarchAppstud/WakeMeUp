package com.wakemeup.reveil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.neocampus.repo.Repository

class ReveilListeViewModel(val repo: Repository) : ViewModel() {

    private val reveilListeState = MediatorLiveData<Map<Int, ReveilModel>>()

    init {
        reveilListeState.addSource(repo.getReveils()) { newReveils ->
            reveilListeState.value = newReveils
        }
    }

    fun getReveilsListeLiveData(): LiveData<Map<Int, ReveilModel>> = reveilListeState

    fun addReveil(newClock: ReveilModel, idReveil: Int) {
        repo.addReveil(newClock, idReveil)
    }

    fun removeReveil(i: Int) {
        repo.removeReveil(i)
    }

    fun editReveil(newClock: ReveilModel, i: Int) {
        repo.editReveil(newClock, i)
    }

    fun switchReveil(idReveil: Int) {
        repo.switchReveil(idReveil)
    }
}