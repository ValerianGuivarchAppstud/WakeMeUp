package com.vguivarc.wakemeup.reveil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.repo.Repository

class ReveilListeViewModel(val repo: Repository) : ViewModel() {

    private val reveilListeState = MediatorLiveData<Map<Int, ReveilModel>>()

    init {
        reveilListeState.addSource(repo.getReveilsLiveData()) { newReveils ->
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

    fun snoozeReveil(idReveil: Int) {
        repo.snoozeReveil(idReveil)
    }

    fun stopReveil(idReveil: Int) {
        repo.stopReveil(idReveil)

    }
}