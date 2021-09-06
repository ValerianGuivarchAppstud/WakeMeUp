package com.vguivarc.wakemeup.transport.sonnerie

import androidx.lifecycle.*
import com.vguivarc.wakemeup.data.provider.Repository
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.domain.external.entity.UserProfile

class SonnerieListeViewModel(val repo: Repository) : ViewModel() {

    private val sonneriesPasseesListeState = MediatorLiveData<Map<String, Ringing>>()
    private val sonneriesAttenteListeState = MediatorLiveData<Map<String, Ringing>>()
    private val listeVueListeState = MediatorLiveData<Boolean>()

    fun getListePasseesLiveData(): LiveData<Map<String, Ringing>> = sonneriesPasseesListeState
    fun getListeAttenteLiveData(): LiveData<Map<String, Ringing>> = sonneriesAttenteListeState
    fun getListeVueLiveData(): LiveData<Boolean> = listeVueListeState

    fun utilisationSonnerie(ringing: Ringing) {
    }

/*    fun updateSonneries() {
        repo.updateSonneriesRecues()
    }*/

    fun listeAffichee() {
    }

    fun deleteSonneriePassee(ringing: Ringing) {
    }

    fun addSonnerieUrlToUser(lco: LifecycleOwner, url: String, contact: UserProfile?, senderName: String?) {
    }

    fun addSonnerieToUser(song: Song, contact: UserProfile) {
    }

/*    fun getSonneriesEnvoyees(): LiveData<List<Ringing>> {
        return repo.getSonneriesEnvoyees()
    }*/
}
