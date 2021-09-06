package com.vguivarc.wakemeup.viewmodel

/*
class ContactListViewModel(
    private val contactsService: ContactService
) : BaseViewModel() {

    private val _contactsList = MutableLiveData<Resource<List<Contact>>>()
    val contactList: LiveData<Resource<List<Contact>>>
        get() = _contactsList

    private val _ringingHasBeenShared = SingleLiveEvent<Resource<Ringing>>()
    val ringingHasBeenShared: LiveData<Resource<Ringing>>
        get() = _ringingHasBeenShared

    fun getContactList() {
        contactsService.getContactList()
            .applySchedulers()
            .doOnSubscribe { _contactsList.postValue(Loading()) }
            .subscribe(
                {
                    _contactsList.postValue(Success(it))
                },
                {
                    _contactsList.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }

    fun shareRinging(shareRingingRequest: ShareRingingRequest) {
        contactsService.shareRinging(shareRingingRequest)
            .applySchedulers()
            .doOnSubscribe {
                _ringingHasBeenShared.postValue(Loading())
            }
            .subscribe(
                { ringing ->
                    _ringingHasBeenShared.postValue(Success(ringing))
                },
                {
                    _ringingHasBeenShared.postValue(Fail(it))
                }
            )
            .addTo(disposables)
    }
}
*/