package com.vguivarc.wakemeup.transport.favoritelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.external.ContactInteractor
import com.vguivarc.wakemeup.domain.external.FavoriteInteractor
import com.vguivarc.wakemeup.domain.external.SessionInteractor
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.internal.IFavoriteProvider
import com.vguivarc.wakemeup.transport.contactlistfacebook.ContactFacebookListSideEffect
import com.vguivarc.wakemeup.transport.contactlistfacebook.ContactFacebookListState
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo
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
                state.copy(favoriteList = list, isLoading = false)
            }

        } catch (exception: Exception) {
            Timber.e(exception)

            reduce {
                state.copy(favoriteList = emptyList(), isLoading = false)
            }

//            postSideEffect(ContactFacebookListSideEffect.Toast(R.string.general_error))
        }
    }



    fun saveFavoriteStatus(favorite: Favorite, isFavorite: Boolean) = intent {

        try {
            val list = favoriteInteractor.saveFavoriteStatus(favorite.song, isFavorite)

            reduce {
                state.copy(favoriteList = list, isLoading = false)
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