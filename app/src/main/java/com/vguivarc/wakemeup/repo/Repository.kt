package com.vguivarc.wakemeup.repo

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.LinkFirebaseAndFacebookIds
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.connect.ui.ConnectResult
import com.vguivarc.wakemeup.contact.Contact
import com.vguivarc.wakemeup.contact.ContactResult
import com.vguivarc.wakemeup.facebook.FacebookResult
import com.vguivarc.wakemeup.notification.MyFirebaseMessagingService
import com.vguivarc.wakemeup.notification.NotificationMusicMe
import com.vguivarc.wakemeup.notification.Token
import com.vguivarc.wakemeup.reveil.ReveilModel
import com.vguivarc.wakemeup.song.Song
import com.vguivarc.wakemeup.song.favori.Favori
import com.vguivarc.wakemeup.song.favori.VideoFavoriResult
import com.vguivarc.wakemeup.song.search.SearchYouTube
import com.vguivarc.wakemeup.song.search.SearchYouTubeOneSong
import com.vguivarc.wakemeup.song.search.VideoSearchResult
import com.vguivarc.wakemeup.sonnerie.Sonnerie
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult
import org.json.JSONException
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Repository() {
    /*************************************************/
    /********************** USER *********************/
    /*************************************************/
    val auth: FirebaseAuth
    private var currentUser: UserModel? = null
    private var currentUserLiveData= MutableLiveData<UserModel?>()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()


    fun getUserModelAfterConnection(){
        if(auth.currentUser!=null) {
            val reference =
                database.getReference("Users")
                    .child(auth.currentUser!!.uid)

            reference.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                        currentUser = user
                        currentUserLiveData.value = currentUser
                        updateSonneriesEnvoyees()
                        updateSonneriesRecues()
                        updateNotification()
                        getFavoris()
                        getContact()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
            )
        } else {
            currentUser = null
            currentUserLiveData.value = currentUser
        }
    }
    init {
        auth = FirebaseAuth.getInstance()
        getUserModelAfterConnection()
    }

    fun getCurentUserLiveData() : LiveData<UserModel?> = currentUserLiveData
    private val _signupResult = MutableLiveData<ConnectResult>()
    val signupResult: LiveData<ConnectResult> = _signupResult



    fun createUserWithEmailAndPassword(username : String, mail: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener() { it ->
            if (it.isSuccessful) {
                val firebaseUser = auth.currentUser!!
                val userId = firebaseUser.uid
                val reference = database.getReference("Users").child(userId)
                val hashmap = hashMapOf<String, String>()
                hashmap["id"] = userId
                hashmap["imageUrl"] = ""
                //hashmap["phone"] = phone
                hashmap["username"] = username
                //TODO hashmap["video_favori"] = "vide"
                reference.setValue(hashmap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _signupResult.value =
                            ConnectResult(auth.currentUser)
                        FirebaseInstanceId.getInstance().instanceId
                            .addOnSuccessListener { instanceIdResult ->
                                val token = instanceIdResult.token
                                MyFirebaseMessagingService.registerInstanceOfUser(
                                    Token(token),
                                    auth.currentUser,
                                    auth.currentUser!!.uid
                                )
                            }
                    } else {
                        _signupResult.value =
                            ConnectResult(error = R.string.signup_failed)
                    }
                }
            } else {
                _signupResult.value =
                    ConnectResult(error = R.string.signup_failed)
            }
        }
    }



    fun createUserWithFacebook(accessToken :AccessToken) {
                val firebaseUser = auth.currentUser!!
                val reference = database.getReference("Users").child(firebaseUser.uid)
                val hashmap = hashMapOf<String, String>()
                hashmap["id"] = firebaseUser.uid
                hashmap["imageUrl"] = firebaseUser.photoUrl.toString()
                hashmap["username"] = firebaseUser.displayName.toString()

                reference.setValue(hashmap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val rootRef =
                            database.reference.child("LinkFirebaseAndFacebookIds")
                        val query: Query =
                            rootRef.orderByChild("idFirebase").equalTo(auth.currentUser?.uid!!)
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("LinkFirebaseAndFbIds", "cancelled " + p0.message)
                                _signupResult.value =
                                    ConnectResult(error = R.string.signup_failed)
                            }
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.childrenCount.toInt() == 0) {
                                    val referenceCont = database.getReference("LinkFirebaseAndFacebookIds")
                                    val link = LinkFirebaseAndFacebookIds(auth.currentUser!!.uid, accessToken.userId)
                                    val refLinkPush = referenceCont.push()
                                    val keyLink = refLinkPush.key!!
                                    refLinkPush.setValue(link).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            _signupResult.value =
                                                ConnectResult(auth.currentUser)
                                        } else {
                                            _signupResult.value =
                                                ConnectResult(error = R.string.signup_failed)
                                        }
                                    }
                                } else {
                                    //TODO tester si l'id FB est tjr le même, si non, modifier.. A moins que ça soit jamais possible simplement ?
                                    _signupResult.value =
                                        ConnectResult(auth.currentUser)
                                }
                            }
                        })
                        /*
                       //Exemple Messaging ?

                        FirebaseInstanceId.getInstance().instanceId
                            .addOnSuccessListener { instanceIdResult ->
                                val token = instanceIdResult.token
                                MyFirebaseMessagingService.registerInstanceOfUser(
                                    Token(token),
                                    auth.currentUser,
                                    auth.currentUser!!.uid
                                )
                            }*/
                    } else {
                        _signupResult.value =
                            ConnectResult(error = R.string.signup_failed)

                }
        }
    }


    //TODO liveDataResult pour l'envoie du mail
    fun sendEmailConfirm() {
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /*Toast.makeText(
                        applicationContext,
                        "Un email de confirmation vous a été envoyé ;)",
                        Toast.LENGTH_LONG
                    ).show()*/
                }
            }
    }


    private val _loginResult = MutableLiveData<ConnectResult>()
    val loginResult: LiveData<ConnectResult> = _loginResult

    fun signInWithEmailAndPassword(mail: String, password: String) {
        auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener {
            if (it.isSuccessful){// && AppWakeUp.auth.currentUser!!.isEmailVerified) {
                _loginResult.value =
                    ConnectResult(auth.currentUser)
                getUserModelAfterConnection()
            } else {
                Timber.e(it.exception.toString())
                disconnect()
                _loginResult.value =
                    ConnectResult(error = R.string.login_failed)
            }
        }
    }


    fun signInWithCredential(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ it ->
                if (it.isSuccessful){// && AppWakeUp.auth.currentUser!!.isEmailVerified) {
                    when(credential.provider){
                        "facebook.com"-> {
                            createUserWithFacebook(accessToken)
                            _loginResult.value =
                                ConnectResult(auth.currentUser)
                        }
                        else -> {
                            _loginResult.value =
                            ConnectResult(auth.currentUser)
                        }
                    }
                    getUserModelAfterConnection()
                } else {
                    Timber.e(it.exception.toString())
                    Timber.e( "signInWithCredential:failure"+ it.exception)
                    disconnect()
                    _loginResult.value =
                        ConnectResult(error = R.string.login_failed)
                }
            }
    }

    //TODO quand disconnect, go activity connexion plutôt que laisser activity réveil pas connecté

    fun disconnect() {
        auth.signOut()
        currentUser = null
        currentUserLiveData.value = currentUser
        _loginResult.value = null
        _signupResult.value = null
    }


    /*************************************************/
    /******************** REVEILS ********************/
    /*************************************************/

    private val reveils = sortedMapOf<Int, ReveilModel>()
    private val reveilsLiveData = MutableLiveData<Map<Int, ReveilModel>>()
    fun getReveilsLiveData(): LiveData<Map<Int, ReveilModel>> = reveilsLiveData


    fun addReveil(reveilModel: ReveilModel, index: Int) {
        reveils.put(index, reveilModel)
        reveilModel.calculeNextCalendar()
        reveilModel.startAlarm()
        enregistrementReveil()
        reveilsLiveData.value = reveils
    }

    fun snoozeReveil(idReveil: Int) {
        reveils[idReveil]!!.snooze()
    }

    fun stopReveil(idReveil: Int) {
        reveils[idReveil]!!.stop()
    }

    fun editReveil(reveilModel: ReveilModel, index: Int) {
        reveilModel.cancelAlarm()
        removeReveil(index)
        reveilModel.calculeNextCalendar()
        reveilModel.startAlarm()
        addReveil(reveilModel, index)
    }

    fun removeReveil(index: Int) {
        reveils[index]!!.cancelAlarm()
        reveils.remove(index)
        enregistrementReveil()
        reveilsLiveData.value = reveils
    }

    private fun enregistrementReveil() {
        val fileOutput =
            AppWakeUp.appContext.openFileOutput(AppWakeUp.NAME_FILE_REVEIL, Context.MODE_PRIVATE)
        val outputStream = ObjectOutputStream(fileOutput)
        outputStream.writeObject(reveils)
    }

    fun chargement() {
        try {
            val fileInput = AppWakeUp.appContext.openFileInput(AppWakeUp.NAME_FILE_REVEIL)
            val inputStream = ObjectInputStream(fileInput)
            val listReveil = inputStream.readObject() as SortedMap<Int, ReveilModel>
            reveils.putAll(listReveil)
            reveilsLiveData.value = reveils
            ReveilModel.chargement(reveils)
        } catch (e: FileNotFoundException) {

        } catch (e : java.lang.Exception){
            reveilsLiveData.value = mutableMapOf()
            enregistrementReveil()
        }
        try {
            val fileInput = AppWakeUp.appContext.openFileInput(AppWakeUp.NAME_FILE_HISTORIQUE)
            val inputStream = ObjectInputStream(fileInput)
            val loadedHistoriqueList = inputStream.readObject() as MutableMap<String, Long>
            historiqueList.putAll(loadedHistoriqueList)
            historiqueLiveData.value = historiqueList
        } catch (e: FileNotFoundException) {

        }
    }

    fun switchReveil(idReveil: Int) {
        reveils[idReveil]!!.isActif = !reveils[idReveil]!!.isActif
        if (reveils[idReveil]!!.isActif)
            reveils[idReveil]!!.startAlarm()
        else
            reveils[idReveil]!!.cancelAlarm()
    }

    /**************************************************/
    /******************** CONTACTS ********************/
    /**************************************************/
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


    private val facebookFriendsList = mutableMapOf<String, UserModel>()
    private val facebookListLiveData = MutableLiveData<FacebookResult>()
    fun getFacebookListLiveData(): LiveData<FacebookResult> = facebookListLiveData

    fun requestFacebookFriendData(){

        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), GraphRequest.GraphJSONObjectCallback() { obj, response->
            GraphRequest(
                AccessToken.getCurrentAccessToken(),  // "/me/friends",
                //"me/taggable_friends",
                "me/friends",
                null,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    try {
                        val rawName = response.jsonObject.getJSONArray("data")
                        contactsList.clear()
                        if (rawName.length() == 0) {
                            facebookFriendsList.clear()
                            facebookListLiveData.value =
                                FacebookResult(facebookFriendsList)
                        } else {
                            val listIdFb = mutableListOf<String>()
                            for (i in 0 until rawName.length()) {
                                listIdFb.add(rawName.getJSONObject(i).getString("id"))
                            }

                            val iter = listIdFb.iterator()
                            facebookFriendsList.clear()
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
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ).executeAsync()

        })

        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()

    }


    private fun getFacebookFriends(iter: MutableIterator<String>) {
        if (iter.hasNext()) {
            val fbId = iter.next()

            val rootRef =
                database.reference.child("LinkFirebaseAndFacebookIds")//.equalTo(currentUser?.uid!!, "appartientA")
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
                            val fbLink =
                                dataSnapshot.getChildren().first().getValue(LinkFirebaseAndFacebookIds::class.java)!!
                        val referenceFb2 = database.getReference("Users")
                            .child(fbLink.idFirebase)
                            referenceFb2.addValueEventListener(
                                object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val friend: UserModel =
                                            dataSnapshot.getValue(UserModel::class.java)!!
                                        facebookFriendsList.put(friend.id, friend)
                                        getFacebookFriends(iter)
                                    }
                                }
                            )
                    }
                })
        }
        else {
            facebookListLiveData.value=FacebookResult(facebookFriendsList)
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



                  /*  favorisList.put(fbId.key!!, fav)

                    if(dataIterator.hasNext()){
                        getSongForFavori(dataIterator)
                    } else {
                        favorisListLiveData.value =
                            VideoFavoriResult(favoriList = favorisList)
                    }*/


    fun getContact() {
        val rootRef =
            database.reference.child("Contact")//.equalTo(currentUser?.uid!!, "appartientA")

        if (auth.currentUser == null) {
            contactListLiveData.value = ContactResult()
        } else {
            val query: Query =
                rootRef.orderByChild("ajoutePar").equalTo(auth.currentUser?.uid!!)

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
    }


    /*************************************************/
    /******************* RECHERCHE********************/
    /*************************************************/

    private val historiqueList = mutableMapOf<String, Long>()
    private val historiqueLiveData = MutableLiveData<Map<String, Long>>()
    private var lastSearch: String = ""
    fun getVideoSearchHistorique(): MutableLiveData<Map<String, Long>> = historiqueLiveData

    fun notifyNewSortedType() {
        historiqueLiveData.value = historiqueLiveData.value
    }


    private val _videoSearchResult = MutableLiveData<VideoSearchResult>()
    private val videoSearchResult: LiveData<VideoSearchResult> = _videoSearchResult
    fun getVideoSearchResult(): LiveData<VideoSearchResult> = videoSearchResult
    private var searchYouTube: SearchYouTube? = null


    //TODO empêcher l'appel en double ici
    private val _videoOneSearchResult = MutableLiveData<VideoSearchResult>()
    private val videoOneSearchResult: LiveData<VideoSearchResult> = _videoOneSearchResult
    fun getOneVideoSearchResult(): LiveData<VideoSearchResult> = videoOneSearchResult
    private var searchOneYouTube: SearchYouTubeOneSong? = null

    fun addHistorique(historique: String) {
        historiqueList.put(historique, Timestamp.now().seconds)
        historiqueLiveData.value = historiqueList
        enregistrementHistorique()
    }

    private fun enregistrementHistorique() {
        val fileOutput =
            AppWakeUp.appContext.openFileOutput(
                AppWakeUp.NAME_FILE_HISTORIQUE,
                Context.MODE_PRIVATE
            )
        val outputStream = ObjectOutputStream(fileOutput)
        outputStream.writeObject(historiqueList)
        chargement()
    }

    fun searchVideos(query: String, nbRecherche: Int) {
        addHistorique(query)
        lastSearch = query
        searchYouTube = SearchYouTube(_videoSearchResult)
        searchYouTube!!.execute(query, "" + nbRecherche)
    }

    fun addSearchVideo(nbRecherche: Int) {
        if (searchYouTube != null) {
            if (searchYouTube!!.status != AsyncTask.Status.RUNNING) {
                searchYouTube = SearchYouTube(_videoSearchResult)
                searchYouTube!!.execute(
                    lastSearch,
                    "" + (_videoSearchResult.value!!.searchList.size + nbRecherche)
                )
            }
        }
    }

    /*************************************************/
    /******************** FAVORIS*********************/
    /*************************************************/

    //TODO bug : quand on clique sur un contact, et qu'on revient en arrière, les onglets sont pas bons

    private val favorisList = mutableMapOf<String, Favori>()

    private val favorisListLiveData = MutableLiveData<VideoFavoriResult>()
    fun getFavorisListLiveData(): LiveData<VideoFavoriResult> = favorisListLiveData

    private val favoriStateAddResult = MutableLiveData<AddFireBaseObjectResult>()
    fun getFavoriStateAddResult(): MutableLiveData<AddFireBaseObjectResult> = favoriStateAddResult

    //todo marche pas, le test de si le favori existe déjà
    fun addFavori(song: Song) {
        val rootRef =
            database.reference.child("Favoris")//.equalTo(currentUser?.id!!, "appartientA")
            val query: Query =
                rootRef.orderByChild("appartientA").equalTo(currentUser?.id!!)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    val list = p0.children.toList()
                    if ((list.filter { fav -> fav.getValue(Favori::class.java)!!.idSong == song.id }).size > 0) {
                        favoriStateAddResult.value =
                            AddFireBaseObjectResult(error = Exception("AlreadyExistingFavori"))
                    } else {
                        val referenceFav = database.getReference("Favoris")
                        val currentuser = currentUser!!
                        val favori = Favori(getNowTxt(), song.id, currentuser.id)
                        val rootRef = database.reference.child("Song").child(song.id)
                        rootRef.setValue(song).addOnCompleteListener {
                            if (it.isSuccessful) {
                                val refFavPush = referenceFav.push()
                                val keyFav = refFavPush.key!!
                                refFavPush.setValue(favori).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        favoriStateAddResult.value =
                                            AddFireBaseObjectResult(keyFav)
                                    } else {
                                        favoriStateAddResult.value =
                                            AddFireBaseObjectResult(error = it.exception)
                                    }
                                }
                            } else {
                                favoriStateAddResult.value =
                                    AddFireBaseObjectResult(error = it.exception)
                            }
                        }
                    }
                }
            })

    }

    fun deleteFavori(song: Song) {
        val keyFavToDelete = favorisList.filterValues { it.idSong == song.id }.keys.toList()[0]
        database.getReference("Favoris").child(keyFavToDelete).removeValue()
    }


    private fun getNowTxt(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date()
        val strDate: String = dateFormat.format(date).toString()
        return strDate
    }

    fun getFavoris() {
        val rootRef =
            database.reference.child("Favoris")//.equalTo(currentUser?.id!!, "appartientA")

        if (currentUser == null) {
            favorisListLiveData.value = VideoFavoriResult()
        } else {
            val query: Query =
                rootRef.orderByChild("appartientA").equalTo(currentUser?.id!!)

            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("RepositoryGetFavoris", "cancelled " + p0.message)
                    favorisListLiveData.value = VideoFavoriResult(error = p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    favorisList.clear()
                    if (p0.childrenCount.toInt() == 0) {
                        favorisListLiveData.value =
                            VideoFavoriResult(favoriList = favorisList)
                    } else {
                        val dataIterator = p0.getChildren().iterator()
    //                    while (dataIterator.hasNext()) {
                        getSongForFavori(dataIterator)
//                        }
  //                      for (ds in p0.getChildren()) {

                        }
                    }
                //}
            })
        }
    }

    fun getSongForFavori(dataIterator: MutableIterator<DataSnapshot>) {
        val ds = dataIterator.next()
        val fav: Favori = ds.getValue(Favori::class.java)!!
        val referenceSong =
            database.getReference("Song").child(fav.idSong)
        referenceSong.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val s: Song = dataSnapshot.getValue(Song::class.java)!!
                    fav.song = s
                    favorisList.put(ds.key!!, fav)

                    if(dataIterator.hasNext()){
                        getSongForFavori(dataIterator)
                    } else {
                        favorisListLiveData.value =
                            VideoFavoriResult(favoriList = favorisList)
                    }
                }
            })
    }

    /**************************************************/
    /**************** SONNERIES RECUES ****************/
    /**************************************************/


    val listSonneriesEnAttente = mutableMapOf<String, Sonnerie>()
    val listSonneriesPassee = mutableMapOf<String, Sonnerie>()
    val listSonneriesEnvoyees = mutableListOf<Sonnerie>()
    private val listSonneriesPasseeLiveData = MutableLiveData<Map<String, Sonnerie>>()
    private val listSonneriesEnAttenteLiveData = MutableLiveData<Map<String, Sonnerie>>()
    private val listSonneriesEnvoyeesLiveData = MutableLiveData<List<Sonnerie>>()
    private val listeVueLiveData = MutableLiveData<Boolean>()

    fun getSonneriesAttente(): LiveData<Map<String, Sonnerie>> = listSonneriesEnAttenteLiveData
    fun getSonneriesPassees(): LiveData<Map<String, Sonnerie>> = listSonneriesPasseeLiveData
    fun getSonneriesEnvoyees(): LiveData<List<Sonnerie>> = listSonneriesEnvoyeesLiveData
    fun getListeVue(): LiveData<Boolean> = listeVueLiveData


    fun listeAffichee() {
        listeVueLiveData.value = false
    }

    fun updateSonneriesEnvoyees() {

        val rootRef =
            database.reference.child("Sonnerie")//.equalTo(currentUser?.id!!, "appartientA")

        val query: Query =
            rootRef.orderByChild("senderId").equalTo(currentUser?.id!!)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listSonneriesEnvoyees.clear()
                for (ds in p0.getChildren()) {
                    val sonnerie = ds.getValue(Sonnerie::class.java)!!
                    listSonneriesEnvoyees.add(sonnerie)
                }
                listSonneriesEnvoyeesLiveData.value = listSonneriesEnvoyees
            }
        })
    }


    fun updateSonneriesRecues() {
        val rootRef =
            database.reference.child("Sonnerie")//.equalTo(currentUser?.id!!, "appartientA")
        val query: Query =
            rootRef.orderByChild("idReceiver").equalTo(currentUser?.id!!)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listSonneriesPassee.clear()
                listSonneriesEnAttente.clear()
                for (ds in p0.getChildren()) {
                    val sonnerie = ds.getValue(Sonnerie::class.java)!!
                    sonnerie.idSonnerie = ds.key!!
                    val rootSong =
                        database.reference.child("Song").child(sonnerie.idSong)
                    if (sonnerie.senderId != "") {
                        val referenceUser =
                            database.getReference("Users").child(sonnerie.senderId)
                        referenceUser.addValueEventListener(
                            object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val u: UserModel =
                                        dataSnapshot.getValue(UserModel::class.java)!!
                                    sonnerie.sender = u
                                    rootSong.addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {}
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val s: Song = dataSnapshot.getValue(Song::class.java)!!
                                            sonnerie.song = s
                                            if (sonnerie.ecoutee) {
                                                addSonneriePassees(ds.key!!, sonnerie)
                                            } else {
                                                addSonnerieEnAttente(ds.key!!, sonnerie)
                                            }
                                        }
                                    })
                                }
                            }
                        )
                    } else {
                        rootSong.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {}
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val s: Song = dataSnapshot.getValue(Song::class.java)!!
                                sonnerie.song = s
                                if (sonnerie.ecoutee) {
                                    addSonneriePassees(ds.key!!, sonnerie)
                                } else {
                                    addSonnerieEnAttente(ds.key!!, sonnerie)
                                }
                            }
                        }
                        )
                    }
                }
            }
        })
    }


    fun addSonnerieEnAttente(
        keySonnerie: String,
        sonnerie: Sonnerie
    ) {
        listSonneriesEnAttente.put(keySonnerie, sonnerie)
        listSonneriesEnAttenteLiveData.value = listSonneriesEnAttente
        listeVueLiveData.value = true
    }

    fun addSonneriePassees(
        keySonnerie: String,
        sonnerie: Sonnerie
    ) {
        listSonneriesPassee.put(keySonnerie, sonnerie)
        listSonneriesPasseeLiveData.value = listSonneriesPassee
    }

    fun utilisationSonnerie(sonnerie: Sonnerie) {
        val k = listSonneriesEnAttente.filter { entry -> entry.value == sonnerie }.keys.toList()[0]
        val hopperRef: DatabaseReference = database.reference.child("Sonnerie").child(k)
        val hopperUpdates: MutableMap<String, Any> = HashMap()
        hopperUpdates["ecoutee"] = true
        hopperRef.updateChildren(hopperUpdates)
        updateSonneriesRecues()
        addNotif(NotificationMusicMe.newInstance_SonnerieUtilisee(sonnerie))
    }

    fun deleteSonneriePassee(sonnerie: Sonnerie) {
        database.getReference("Sonnerie").child(sonnerie.idSonnerie).removeValue()
    }

    private val sonnerieStateAddResult = MutableLiveData<AddFireBaseObjectResult>()
    fun getSonnerieStateAddResult(): MutableLiveData<AddFireBaseObjectResult> =
        sonnerieStateAddResult


    fun addSonnerieToUser(
        song: Song,
        contact: UserModel
    ) {
        val currentuser = currentUser

        val sonnerie = Sonnerie(
            song.id,
            Timestamp.now().seconds,
            false,
            contact.id,
            currentuser!!.id,
            ""
        )
        val referenceSon = database.getReference("Sonnerie")
        val refSonnPush = referenceSon.push()
        val keySonn = refSonnPush.key!!
        sonnerie.idSonnerie = keySonn
        refSonnPush.setValue(sonnerie).addOnCompleteListener {
            if (it.isSuccessful) {
                favoriStateAddResult.value =
                    AddFireBaseObjectResult(keySonn)
                addNotif(NotificationMusicMe.newInstance_Envoie_Musique(sonnerie))
            } else {
                favoriStateAddResult.value =
                    AddFireBaseObjectResult(error = it.exception)
            }
        }
    }

    fun addSonnerieUrlToUser(
        lco: LifecycleOwner,
        url: String,
        contact: UserModel?,
        senderName: String?
    ) {
        //TODO pas possible d'ajouter pkus de X chansons en X temps ?
        val referenceSon = database.getReference("Sonnerie")
        val currentuser = currentUser
        val extractId = if (url.contains("youtu.be")) {
            url.substring(url.indexOf("youtu.be/") + 9)
        } else if (url.contains("youtube.com/watch?")) {
            //https://www.youtube.com/watch?v=q1ludVEd6hw&fbclid=IwAR0zqUjvs_uOGs
            val urlss = url.substring(url.indexOf("/watch?") + 7)
            urlss.substring(urlss.indexOf("&"))
        } else {
            sonnerieStateAddResult.value =
                AddFireBaseObjectResult(error = java.lang.Exception("Url invalide "))
            ""
        }
        if (extractId != "") {

            searchOneYouTube = SearchYouTubeOneSong(_videoOneSearchResult)
            searchOneYouTube!!.execute(extractId)

            _videoOneSearchResult.observe(lco, androidx.lifecycle.Observer {
                if (it.error == null) {
                    val song = Song(
                        it.searchList[0].id,
                        it.searchList[0].title,
                        it.searchList[0].artworkUrl
                    )
                    val sonnerie = if (currentuser != null) {
                        Sonnerie(
                            song.id,
                            Timestamp.now().seconds,
                            false,
                            contact!!.id,
                            currentuser.id,
                            ""
                        )
                    } else {
                        Sonnerie(
                            song.id,
                            Timestamp.now().seconds,
                            false,
                            contact!!.id,
                            "",
                            senderName!!
                        )
                    }

                    val rootRef = database.reference.child("Song").child(sonnerie.idSong)
                    rootRef.setValue(song).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val refSonnPush = referenceSon.push()
                            val keySonn = refSonnPush.key!!
                            sonnerie.idSonnerie = keySonn
                            refSonnPush.setValue(sonnerie).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    favoriStateAddResult.value =
                                        AddFireBaseObjectResult(keySonn)
                                } else {
                                    favoriStateAddResult.value =
                                        AddFireBaseObjectResult(error = it.exception)
                                }
                            }
                        } else {
                            favoriStateAddResult.value =
                                AddFireBaseObjectResult(error = it.exception)
                        }
                    }

                } else {
                    AddFireBaseObjectResult(error = it.error)
                }
            }
            )
        }
    }

    fun addFavoriString(lco: LifecycleOwner, favyt: String) {
        searchOneYouTube = SearchYouTubeOneSong(_videoOneSearchResult)
        searchOneYouTube!!.execute(favyt)

        _videoOneSearchResult.observe(lco, androidx.lifecycle.Observer {
            if (it.error == null) {
                val song = Song(
                    it.searchList[0].id,
                    it.searchList[0].title,
                    it.searchList[0].artworkUrl
                )
                val rootRef = database.reference.child("Song").child(song.id)
                rootRef.setValue(song).addOnCompleteListener {
                    if (it.isSuccessful) {
                        addFavori(song)
                    }
                }
            }
        })
    }

    /**************************************************/
    /***************** NOTIFICATIONS ******************/
    /**************************************************/

    val listNotifications = mutableMapOf<String, NotificationMusicMe>()
    private val listNotificationsLiveData = MutableLiveData<Map<String,NotificationMusicMe>>()
 //   private val notifVueLiveData = MutableLiveData<Boolean>()

    fun getNotifications(): LiveData<Map<String,NotificationMusicMe>> = listNotificationsLiveData
    //fun getNotifVue(): LiveData<Boolean> = notifVueLiveData


    fun notifAffichee() {
     //   notifVueLiveData.value = false
        val listNotifPasVues = listNotifications.filter { entry -> entry.value.vue == false }
        for(n in listNotifPasVues) {
            val hopperRef: DatabaseReference = database.reference.child("Notification").child(n.key)
            val hopperUpdates: MutableMap<String, Any> = HashMap()
            hopperUpdates["vue"] = true
            hopperRef.updateChildren(hopperUpdates)
        }
        updateNotification()
    }

    private fun addNotif(notificationAjoutContact: NotificationMusicMe) {
        val referenceNotif = database.getReference("Notification")
        val refNotifPush = referenceNotif.push()
        refNotifPush.setValue(notificationAjoutContact)

    }

    fun updateNotification() {
        val rootRef =
            database.reference.child("Notification")
        val query: Query =
            rootRef.orderByChild("idReceiver").equalTo(currentUser?.id!!)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listNotifications.clear()
                for (ds in p0.getChildren()) {
                    val notif = ds.getValue(NotificationMusicMe::class.java)!!
                    val referenceUser =
                        database.getReference("Users").child(notif.senderId)
                    referenceUser.addValueEventListener(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {}
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val u: UserModel =
                                    dataSnapshot.getValue(UserModel::class.java)!!
                                notif.sender = u
                                listNotifications.put(ds.key!!, notif)
                                listNotificationsLiveData.value=listNotifications
                            }
                    })
                }
                if(!p0.hasChildren()){
                    listNotificationsLiveData.value=listNotifications
                }
            }
        })
    }

    fun deleteNotif(notifKey: String) {
        database.getReference("Notification").child(notifKey).removeValue()
        updateNotification()
    }


}