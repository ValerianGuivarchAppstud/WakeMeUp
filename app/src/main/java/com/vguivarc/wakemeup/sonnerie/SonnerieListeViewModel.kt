package com.vguivarc.wakemeup.sonnerie

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.repo.Repository
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult

class SonnerieListeViewModel(val repo: Repository) : ViewModel() {

    private val sonneriesPasseesListeState = MediatorLiveData<Map<String, Sonnerie>>()
    private val sonneriesAttenteListeState = MediatorLiveData<Map<String, Sonnerie>>()
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

    fun getListePasseesLiveData(): LiveData<Map<String, Sonnerie>> = sonneriesPasseesListeState
    fun getListeAttenteLiveData(): LiveData<Map<String, Sonnerie>> = sonneriesAttenteListeState
    fun getListeVueLiveData(): LiveData<Boolean> = listeVueListeState


    fun utilisationSonnerie(sonnerie: Sonnerie) {
        repo.utilisationSonnerie(sonnerie)
    }

/*    fun updateSonneries() {
        repo.updateSonneriesRecues()
    }*/

    fun listeAffichee() {
        repo.listeAffichee()
    }

    fun deleteSonneriePassee(sonnerie: Sonnerie) {
        repo.deleteSonneriePassee(sonnerie)
    }

    fun addSonnerieUrlToUser(lco : LifecycleOwner, url: String, contact: FirebaseUser?, senderName : String?) {
        repo.addSonnerieUrlToUser(lco, url, contact, senderName)
    }

    fun addSonnerieToUser(song : Song, contact: FirebaseUser) {
        repo.addSonnerieToUser(song, contact)
    }
    fun getSonnerieStateAddResult(): MutableLiveData<AddFireBaseObjectResult> {
        return repo.getSonnerieStateAddResult()
    }

    fun getSonneriesEnvoyees() : LiveData<List<Sonnerie>> {
        return repo.getSonneriesEnvoyees()
    }

}