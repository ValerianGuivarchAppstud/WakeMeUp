package com.vguivarc.wakemeup.ui.sonnerie

import androidx.lifecycle.*
import com.vguivarc.wakemeup.data.repository.Repository
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.ui.song.Song
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult

class SonnerieListeViewModel(val repo: Repository) : ViewModel() {

    private val sonneriesPasseesListeState = MediatorLiveData<Map<String, Ringing>>()
    private val sonneriesAttenteListeState = MediatorLiveData<Map<String, Ringing>>()
    private val listeVueListeState = MediatorLiveData<Boolean>()

    init {
        sonneriesPasseesListeState.addSource(repo.getSonneriesPassees()) { newListe ->
            sonneriesPasseesListeState.value = newListe
        }
        sonneriesAttenteListeState.addSource(repo.getSonneriesAttente()) { newListe ->
            sonneriesAttenteListeState.value = newListe
        }
        listeVueListeState.addSource(repo.getListeVue()) { bool ->
            listeVueListeState.value = bool
        }
    }

    fun getListePasseesLiveData(): LiveData<Map<String, Ringing>> = sonneriesPasseesListeState
    fun getListeAttenteLiveData(): LiveData<Map<String, Ringing>> = sonneriesAttenteListeState
    fun getListeVueLiveData(): LiveData<Boolean> = listeVueListeState


    fun utilisationSonnerie(ringing: Ringing) {
        repo.utilisationSonnerie(ringing)
    }

/*    fun updateSonneries() {
        repo.updateSonneriesRecues()
    }*/

    fun listeAffichee() {
        repo.listeAffichee()
    }

    fun deleteSonneriePassee(ringing: Ringing) {
        repo.deleteSonneriePassee(ringing)
    }

    fun addSonnerieUrlToUser(lco : LifecycleOwner, url: String, contact: UserModel?, senderName : String?) {
        repo.addSonnerieUrlToUser(lco, url, contact, senderName)
    }

    fun addSonnerieToUser(song : Song, contact: UserModel) {
        repo.addSonnerieToUser(song, contact)
    }
    fun getSonnerieStateAddResult(): MutableLiveData<AddFireBaseObjectResult> {
        return repo.getSonnerieStateAddResult()
    }

    fun getSonneriesEnvoyees() : LiveData<List<Ringing>> {
        return repo.getSonneriesEnvoyees()
    }

}