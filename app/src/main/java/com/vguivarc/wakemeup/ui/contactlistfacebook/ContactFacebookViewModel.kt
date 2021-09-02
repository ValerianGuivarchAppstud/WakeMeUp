package com.vguivarc.wakemeup.ui.contactlistfacebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.ContactFacebook
import com.vguivarc.wakemeup.domain.service.ContactService
import com.vguivarc.wakemeup.domain.service.SessionService

class ContactFacebookViewModel(
    private val sessionService: SessionService,
    private val contactService: ContactService
) : BaseViewModel() {

    private var _contactFacebookList = MediatorLiveData<Resource<List<ContactFacebook>>>()
    val contactFacebookList: LiveData<Resource<List<ContactFacebook>>>
        get() = _contactFacebookList




    fun getContactFacebookList() {
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
    }
}
