package com.vguivarc.wakemeup.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vguivarc.wakemeup.domain.entity.UserProfile
import com.vguivarc.wakemeup.ui.contact.FacebookResult

@Suppress("UNCHECKED_CAST")
class Repository {

    /**************************************************/
    /******************** CONTACTS ********************/
    /**************************************************/
    /*
    private val contactsList = mutableMapOf<String, Contact>()

    private val contactListLiveData = MutableLiveData<ContactResult>()
    fun getContactListLiveData(): LiveData<ContactResult> = contactListLiveData

    private val contactStateAddResult = MutableLiveData<AddFireBaseObjectResult>()
    fun getContactStateAddResult(): MutableLiveData<AddFireBaseObjectResult> = contactStateAddResult

    fun addContact(contactUserModel: UserModel) {
        if (contactListLiveData.value != null && (contactListLiveData.value!!.contactList.filter { cont -> cont.value.idContact == contactUserModel.id }).size > 0) {
            contactStateAddResult.value =
                AddFireBaseObjectResult(error = Exception("AlreadyExistingContact"))
        } else {
            val referenceCont = database.getReference("Contact")
            val currentuser = currentUser!!
            val contact = Contact(contactUserModel.id, currentuser.id)
            val refContPush = referenceCont.push()
            val keyCont = refContPush.key!!
            refContPush.setValue(contact).addOnCompleteListener {
                if (it.isSuccessful) {
                    contactStateAddResult.value =
                        AddFireBaseObjectResult(keyCont)
                    addNotif(NotificationMusicMe.newInstance_AjoutContact(contactUserModel.id, currentuser.id))
                } else {
                    contactStateAddResult.value =
                        AddFireBaseObjectResult(error = it.exception)
                }
            }
        }
    }



    fun deleteContact(contact: UserModel) {
        val keyContToDelete =
            contactsList.filterValues { it.idContact == contact.id }.keys.toList()[0]
        database.getReference("Contact").child(keyContToDelete).removeValue()
    }
*/

    private val facebookFriendsList = mutableMapOf<String, UserProfile>()
    private val facebookListLiveData = MutableLiveData<FacebookResult>()
    fun getFacebookListLiveData(): LiveData<FacebookResult> = facebookListLiveData

    val listeFriendStatic = mutableListOf<String>()

/*    fun requestFacebookFriendData() {
        if (getCurrentUser() == null) {
            facebookFriendsList.clear()
            facebookListLiveData.value =
                FacebookResult(facebookFriendsList)
        } else {

            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { _, _ ->
                GraphRequest(
                    AccessToken.getCurrentAccessToken(), // "/me/friends",
                    // "me/taggable_friends",F
                    getCurrentUser()!!.facebookId + "/friends",
                    null,
                    HttpMethod.GET
                ) { response2 ->
                    try {
                        val rawName = response2.jsonObject.getJSONArray("data")
                        facebookFriendsList.clear()
                        val listIdFb = mutableListOf<String>()
/*
                              if (currentUser!!.facebookId=="110838847438019" || currentUser!!.facebookId=="113065823882601"
                                    || currentUser!!.facebookId=="109836767539634" || currentUser!!.facebookId=="101597975040883") {
                                    listIdFb.add("110838847438019")
                                    listIdFb.add("113065823882601")
                                    listIdFb.add("109836767539634")
                                    listIdFb.add("101597975040883")
                                        listIdFb.remove(currentUser!!.facebookId)
                                } else {
                                    listIdFb.addAll(listeFriendStatic)
                                   listIdFb.remove(currentUser!!.facebookId)
                                   listIdFb.remove("110838847438019")
                                   listIdFb.remove("113065823882601")
                                   listIdFb.remove("109836767539634")
                                   listIdFb.remove("101597975040883")
                               }*/

                        for (i in 0 until rawName.length()) {
                            listIdFb.add(rawName.getJSONObject(i).getString("id"))
                        }

                        val iter = listIdFb.iterator()
                        getFacebookFriends(iter)
/*
                                val rootRef =
                                database.reference.child("LinkFirebaseAndFacebookIds")//.equalTo(currentUser?.uid!!, "appartientA")
                            if (auth.currentUser == null) {
                                contactListLiveData.value = ContactResult()
                            } else {

                         val query: Query =
                                    rootRef.orderByChild("IdFirebase").equalTo(rawName.getJSONObject(0).getString("id"))

                                query.addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        Log.e("RepositoryGetContact", "cancelled " + p0.message)
                                        contactListLiveData.value = ContactResult(error = p0.toException())
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        contactsList.clear()
                                        if (p0.childrenCount.toInt() == 0) {
                                            contactListLiveData.value =
                                                ContactResult(contactList = contactsList)
                                        } else {
                                            for (ds in p0.getChildren()) {
                                                val cont: Contact = ds.getValue(Contact::class.java)!!
                                                cont.user=currentUser
                                                contactsList.put(ds.key!!, cont)
                                                contactListLiveData.value =
                                                    ContactResult(contactList = contactsList)
                                            }
                                        }
                                    }
                                })
                            }

                            for (i in 0 until rawName.length()) {
                                val c = rawName.getJSONObject(i)
                                val name = c.getString("name")
                                val fbId = c.getString("id")
                                Timber.e( "JSON NAME :$name")
                                val phone = c.getJSONObject("picture")
                                Timber.e( "" + phone.getString("data"))
                                val jsonObject = phone.getJSONObject("data")
                                val url = jsonObject.getString("url").toString()
                                Timber.e(
                                    "@@@@" + jsonObject.getString("url").toString()
                                )

                                val cont: Contact = Contact(i.toString(), auth.currentUser!!.uid)
                                contactsList.put(i.toString(), cont)
                                contactListLiveData.value =
                                    ContactResult(contactList = contactsList)
                            }*/
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }.executeAsync()
            }

            val parameters = Bundle()
            parameters.putString("fields", "id,name,link,email,picture")
            request.parameters = parameters
            request.executeAsync()
        }
    }

    private fun getFacebookFriends(iter: MutableIterator<String>) {
        if (iter.hasNext()) {
            val fbId = iter.next()

            val rootRef =
                database.reference.child("LinkFirebaseAndFacebookIds") // .equalTo(currentUser?.uid!!, "appartientA")
            val query: Query =
                rootRef.orderByChild("idFacebook").equalTo(fbId)

            /*  val referenceFb =
                database.getReference("LinkFirebaseAndFacebookIds").orderByChild("idFacebook")
                    .equalTo(fbId)*/
            //   .child(auth.currentUser!!.uid)
            query.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //      for (ds in dataSnapshot.getChildren()) {
                        if (dataSnapshot.hasChildren()) {
                            val fbLink =
                                dataSnapshot.children.first()
                                    .getValue(LinkFirebaseAndFacebookIds::class.java)!!
                            val referenceFb2 = database.getReference("Users")
                                .child(fbLink.idFirebase)
                            referenceFb2.addValueEventListener(
                                object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val friend: UserModel =
                                            dataSnapshot.getValue(UserModel::class.java)!!
                                        facebookFriendsList[friend.id] = friend
                                        getFacebookFriends(iter)
                                    }
                                }
                            )
                        } else {
                            getFacebookFriends(iter)
                        }
                    }
                })
        } else {
            // getFixedFacebookFriends()
            facebookListLiveData.value = FacebookResult(facebookFriendsList)
/*          for(f in facebookFriendsList){
              Timber.e("FRIEND -> "+f.value.username)

              val cont = Contact(f.key, currentUser!!.id)
              cont.user=f.value
              contactsList.put(f.key, cont)
              contactListLiveData.value =
                  ContactResult(contactList = contactsList)
          }*/
        }
    }
*/
}
