package com.vguivarc.wakemeup.ui.contactlistfacebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.service.ContactService
import com.vguivarc.wakemeup.domain.service.SessionService
import com.vguivarc.wakemeup.util.applySchedulers
import io.reactivex.rxkotlin.addTo

class ContactFacebookViewModel(
    private val sessionService: SessionService,
    private val contactService: ContactService
) : BaseViewModel() {

    private var _contactFacebookList = MutableLiveData<Resource<List<ContactFacebook>>>()
    val contactFacebookList: LiveData<Resource<List<ContactFacebook>>>
        get() = _contactFacebookList



    fun getContactFacebookList(socialAuthToken: String) {
        contactService.getContactFacebookList(socialAuthToken)
            .applySchedulers()
            .doOnSubscribe { _contactFacebookList.postValue(Loading()) }
            .subscribe(
                {
                    _contactFacebookList.postValue(Success(it))
                },
                {
                    _contactFacebookList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

   /* fun getContactFacebookList() {
        sessionService.getUserProfile()?.facebookId?.let { facebookId ->
            _contactFacebookList =MediatorLiveData<Resource<List<ContactFacebook>>>()
                _contactFacebookList.postValue(Loading())
            _contactFacebookList.addSource(contactService.getContactFacebookList(facebookId)) { fbList ->
            _contactFacebookList.postValue(
                if (fbList.error == null) {
                    Success(fbList.list ?: listOf())
                } else {
                    Fail(fbList.error)
                }
            )
        } ?: apply {
            _contactFacebookList.postValue(Fail(Exception("Not connected")))
        }
        }
/*        sessionService.getUserProfile()?.facebookId?.let { facebookId ->
            _contactFacebookList.postValue(Loading())
            val temp = contactFacebookService.getContactFacebookList(facebookId)
            Transformations.map(temp) {
                if (it.error == null) {
                    _contactFacebookList.postValue(Success(it.list ?: listOf()))
                } else {
                    _contactFacebookList.postValue(Fail(it.error))
                }
            }
        } ?: apply {
            _contactFacebookList.postValue(Fail(Exception("Not connected")))
        }*/
    }*/
}
