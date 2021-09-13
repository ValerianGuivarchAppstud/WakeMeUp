package com.vguivarc.wakemeup.transport.favoritelist

import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.domain.external.FavoriteInteractor
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class FavoriteListViewModel(
    private val favoriteInteractor: FavoriteInteractor
) :
    ContainerHost<FavoriteListState, FavoriteListSideEffect>, ViewModel() {

    override val container =
        container<FavoriteListState, FavoriteListSideEffect>(
            FavoriteListState(),
            onCreate = ::onCreate
        )

    private fun onCreate(initialState: FavoriteListState) {
        getFavoriteList()
    }

    fun getFavoriteList() = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            val list = favoriteInteractor.getFavoriteList()
            reduce {
                state.copy(favoriteList = list.toList(), isLoading = false, currentSong = null)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(favoriteList = emptyList(), isLoading = false, currentSong = null)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }


    fun selectSong(searchSong: Favorite)  = intent {
        reduce {
            state.copy(isLoading = false, currentSong = searchSong)
        }
    }



    fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean) = intent {

        try {
            val list = favoriteInteractor.saveFavoriteStatus(favorite.song, isFavorite)

            reduce {
                state.copy(favoriteList = list.toList(), isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(favoriteList = emptyList(), isLoading = false)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }
}
/*
class FavoriteListViewModel(private val favoriteService: IFavoriteProvider) : BaseViewModel() {

    private val _favoriteList = MutableLiveData<Resource<List<Favorite>>>()
    val favoriteList: LiveData<Resource<List<Favorite>>>
        get() = _favoriteList

    private val _favoriteStatus = MutableLiveData<Resource<Boolean>>()
    val favoriteStatus: LiveData<Resource<Boolean>>
        get() = _favoriteStatus

    fun getFavoriteList() {
        favoriteService.getFavoriteList()
            .applySchedulers()
            .doOnSubscribe { _favoriteList.postValue(Loading()) }
            .subscribe(
                {
                    _favoriteList.postValue(Success(it))
                },
                {
                    _favoriteList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

    fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean) {
        favoriteService.saveFavoriteStatus(favorite.song, isFavorite)
            .applySchedulers()
            .doOnSubscribe { _favoriteStatus.postValue(Loading()) }
            .subscribe(
                {
                    _favoriteStatus.postValue(Success(isFavorite))
                },
                {
                    _favoriteStatus.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
}
*/