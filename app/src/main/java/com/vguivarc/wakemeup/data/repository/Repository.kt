package com.vguivarc.wakemeup.data.repository

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.google.firebase.Timestamp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.LinkFirebaseAndFacebookIds
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.ui.connect.viewmodel.ConnectResult
import com.vguivarc.wakemeup.ui.contact.FacebookResult
import com.vguivarc.wakemeup.notification.NotificationMusicMe
import com.vguivarc.wakemeup.domain.entity.Alarm
import com.vguivarc.wakemeup.ui.song.Song
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.ui.favori.VideoFavoriResult
import com.vguivarc.wakemeup.ui.search.SearchYouTube
import com.vguivarc.wakemeup.ui.search.SearchYouTubeOneSong
import com.vguivarc.wakemeup.ui.search.VideoSearchResult
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.util.AddFireBaseObjectResult
import org.json.JSONException
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNCHECKED_CAST")
class Repository {
    /*************************************************/
    /********************** USER *********************/
    /*************************************************/
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private var currentUser: UserModel? = null
    private var currentUserLiveData= MutableLiveData<UserModel?>()
    fun getCurrentUser() = currentUserLiveData.value
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private fun getUserModelAfterConnection(){
        if(auth.currentUser!=null) {
            val reference =
                database.getReference("Users")
                    .child(auth.currentUser!!.uid)

            reference.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                        currentUserLiveData.value = user
                        updateSonneriesEnvoyees()
                        updateSonneriesRecues()
                        updateNotification()
                        getFavoris()
                        requestFacebookFriendData()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
            )
        } else {
            currentUserLiveData.value = null
        }
    }
    init {
        getUserModelAfterConnection()
    }

    fun getCurentUserLiveData() : LiveData<UserModel?> = currentUserLiveData
    private val _signupResult = MutableLiveData<ConnectResult>()





    private fun createUserWithFacebook(accessToken: AccessToken) {
        val firebaseUser = auth.currentUser!!
        val reference = database.getReference("Users").child(firebaseUser.uid)
        val hashmap = hashMapOf<String, String>()
        hashmap["id"] = firebaseUser.uid
        hashmap["imageUrl"] = firebaseUser.photoUrl.toString()
        hashmap["username"] = firebaseUser.displayName.toString()
        hashmap["facebookId"] = accessToken.userId


                reference.setValue(hashmap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val rootRef =
                            database.reference.child("LinkFirebaseAndFacebookIds")
                        val query: Query =
                            rootRef.orderByChild("idFirebase").equalTo(auth.currentUser?.uid!!)
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                _signupResult.value =
                                    ConnectResult(error = R.string.signup_failed)
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.childrenCount.toInt() == 0) {
                                    val referenceCont =
                                        database.getReference("LinkFirebaseAndFacebookIds")
                                    val link = LinkFirebaseAndFacebookIds(
                                        auth.currentUser!!.uid,
                                        accessToken.userId
                                    )
                                    val refLinkPush = referenceCont.push()
                                    refLinkPush.setValue(link).addOnCompleteListener { it2 ->
                                        if (it2.isSuccessful) {
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
                    } else {
                        _signupResult.value =
                            ConnectResult(error = R.string.signup_failed)

                }
        }
    }


    private val _loginResult = MutableLiveData<ConnectResult>()
    val loginResult: LiveData<ConnectResult> = _loginResult


    fun signInWithCredential(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    when(credential.provider){
                        "facebook.com" -> {
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
                    Timber.e("signInWithCredential:failure${it.exception}")
                    disconnect()
                    _loginResult.value =
                        ConnectResult(error = R.string.login_failed)
                }
            }
    }

    //TODO quand disconnect, go activity connexion plutôt que laisser activity réveil pas connecté

    fun disconnect() {
        auth.signOut()
        currentUserLiveData.value = null
        _loginResult.value = null
        _signupResult.value = null
        getUserModelAfterConnection()
    }



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

    private val facebookFriendsList = mutableMapOf<String, UserModel>()
    private val facebookListLiveData = MutableLiveData<FacebookResult>()
    fun getFacebookListLiveData(): LiveData<FacebookResult> = facebookListLiveData

    val listeFriendStatic = mutableListOf<String>()

    fun requestFacebookFriendData() {
        if (getCurrentUser() == null) {
            facebookFriendsList.clear()
            facebookListLiveData.value =
                FacebookResult(facebookFriendsList)
        } else {

            val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { _, _ ->
                GraphRequest(
                    AccessToken.getCurrentAccessToken(),  // "/me/friends",
                    //"me/taggable_friends",
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
        }
        else {
            //getFixedFacebookFriends()
            facebookListLiveData.value= FacebookResult(facebookFriendsList)
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

/*
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
*/

    /*************************************************/
    /******************* RECHERCHE********************/
    /*************************************************/

    private val historiqueList = mutableMapOf<String, Long>()
    private val historiqueLiveData = MutableLiveData<Map<String, Long>>()
    private var lastSearch: String = ""
    fun getVideoSearchHistorique(): MutableLiveData<Map<String, Long>> = historiqueLiveData


    private val _videoSearchResult = MutableLiveData<VideoSearchResult>()
    private val videoSearchResult: LiveData<VideoSearchResult> = _videoSearchResult
    fun getVideoSearchResult(): LiveData<VideoSearchResult> = videoSearchResult
    private var searchYouTube: SearchYouTube? = null


    //TODO empêcher l'appel en double ici
    private val _videoOneSearchResult = MutableLiveData<VideoSearchResult>()
    private var searchOneYouTube: SearchYouTubeOneSong? = null



    fun searchVideos(query: String, nbRecherche: Int) {
        lastSearch = query
        searchYouTube = SearchYouTube(_videoSearchResult)
        searchYouTube!!.execute(query, "" + nbRecherche)
    }


    /*************************************************/
    /******************** FAVORIS*********************/
    /*************************************************/



    private val favorisList = mutableMapOf<String, Favorite>()

    private val favorisListLiveData = MutableLiveData<VideoFavoriResult>()
    fun getFavorisListLiveData(): LiveData<VideoFavoriResult> = favorisListLiveData

    private val favoriStateAddResult = MutableLiveData<AddFireBaseObjectResult>()
    fun getFavoriStateAddResult(): MutableLiveData<AddFireBaseObjectResult> = favoriStateAddResult

    //todo marche pas, le test de si le favori existe déjà
    fun addFavori(song: Song) {
        val rootRef =
            database.reference.child("Favoris")//.equalTo(currentUser?.id!!, "appartientA")
            val query: Query =
                rootRef.orderByChild("appartientA").equalTo(getCurrentUser()?.id!!)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = p0.children.toList()
                    if ((list.filter { fav -> fav.getValue(Favorite::class.java)!!.song!!.id == song.id }).isNotEmpty()) {
                        favoriStateAddResult.value =
                            AddFireBaseObjectResult(error = Exception("AlreadyExistingFavori"))
                    } else {
                        val referenceFav = database.getReference("Favoris")
                        val favori = Favorite(getNowTxt(), song, getCurrentUser()!!.id)
                        val rootRef2 = database.reference.child("Song").child(song.id)
                        rootRef2.setValue(song).addOnCompleteListener {
                            if (it.isSuccessful) {
                                val refFavPush = referenceFav.push()
                                val keyFav = refFavPush.key!!
                                refFavPush.setValue(favori).addOnCompleteListener { it2 ->
                                    if (it2.isSuccessful) {
                                        favoriStateAddResult.value =
                                            AddFireBaseObjectResult(keyFav)
                                    } else {
                                        favoriStateAddResult.value =
                                            AddFireBaseObjectResult(error = it2.exception)
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
        val keyFavToDelete = favorisList.filterValues { it.song!!.id == song.id }.keys.toList()[0]
        database.getReference("Favoris").child(keyFavToDelete).removeValue()
    }


    private fun getNowTxt(): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date).toString()
    }

    fun getFavoris() {
        val rootRef =
            database.reference.child("Favoris")//.equalTo(currentUser?.id!!, "appartientA")

        if (getCurrentUser() == null) {
            favorisListLiveData.value = VideoFavoriResult()
        } else {
            val query: Query =
                rootRef.orderByChild("appartientA").equalTo(getCurrentUser()?.id!!)

            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    favorisListLiveData.value = VideoFavoriResult(error = p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    favorisList.clear()
                    if (p0.childrenCount.toInt() == 0) {
                        favorisListLiveData.value =
                            VideoFavoriResult(favoriList = favorisList)
                    } else {
                        val dataIterator = p0.children.iterator()
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
        val fav: Favorite = ds.getValue(Favorite::class.java)!!
                    favorisList[ds.key!!] = fav

                    if (dataIterator.hasNext()) {
                        getSongForFavori(dataIterator)
                    } else {
                        favorisListLiveData.value =
                            VideoFavoriResult(favoriList = favorisList)
                    }

    }

    /**************************************************/
    /**************** SONNERIES RECUES ****************/
    /**************************************************/


    val listSonneriesEnAttente = mutableMapOf<String, Ringing>()
    val listSonneriesPassee = mutableMapOf<String, Ringing>()
    val listSonneriesEnvoyees = mutableListOf<Ringing>()
    private val listSonneriesPasseeLiveData = MutableLiveData<Map<String, Ringing>>()
    private val listSonneriesEnAttenteLiveData = MutableLiveData<Map<String, Ringing>>()
    private val listSonneriesEnvoyeesLiveData = MutableLiveData<List<Ringing>>()
    private val listeVueLiveData = MutableLiveData<Boolean>()

    fun getSonneriesAttente(): LiveData<Map<String, Ringing>> = listSonneriesEnAttenteLiveData
    fun getSonneriesPassees(): LiveData<Map<String, Ringing>> = listSonneriesPasseeLiveData
    fun getSonneriesEnvoyees(): LiveData<List<Ringing>> = listSonneriesEnvoyeesLiveData
    fun getListeVue(): LiveData<Boolean> = listeVueLiveData


    fun listeAffichee() {
        listeVueLiveData.value = false
    }

    fun updateSonneriesEnvoyees() {

        val rootRef =
            database.reference.child("Sonnerie")//.equalTo(currentUser?.id!!, "appartientA")

        val query: Query =
            rootRef.orderByChild("senderId").equalTo(getCurrentUser()?.id!!)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listSonneriesEnvoyees.clear()
                for (ds in p0.children) {
                    val sonnerie = ds.getValue(Ringing::class.java)!!
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
            rootRef.orderByChild("idReceiver").equalTo(getCurrentUser()?.id!!)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listSonneriesPassee.clear()
                listSonneriesEnAttente.clear()
                for (ds in p0.children) {
                    val sonnerie = ds.getValue(Ringing::class.java)!!
                    sonnerie.idSonnerie = ds.key!!
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
                                    if (sonnerie.listened) {
                                        addSonneriePassees(ds.key!!, sonnerie)
                                    } else {
                                        addSonnerieEnAttente(ds.key!!, sonnerie)
                                    }
                                }
                            }
                        )
                    } else {

                                if (sonnerie.listened) {
                                    addSonneriePassees(ds.key!!, sonnerie)
                                } else {
                                    addSonnerieEnAttente(ds.key!!, sonnerie)
                                }
                    }
                }
            }
        })
    }


    fun addSonnerieEnAttente(
        keySonnerie: String,
        ringing: Ringing
    ) {
        listSonneriesEnAttente[keySonnerie] = ringing
        listSonneriesEnAttenteLiveData.value = listSonneriesEnAttente
        listeVueLiveData.value = true
    }

    fun addSonneriePassees(
        keySonnerie: String,
        ringing: Ringing
    ) {
        listSonneriesPassee[keySonnerie] = ringing
        listSonneriesPasseeLiveData.value = listSonneriesPassee
    }

    fun utilisationSonnerie(ringing: Ringing) {
        val k = listSonneriesEnAttente.filter { entry -> entry.value == ringing }.keys.toList()[0]
        val hopperRef: DatabaseReference = database.reference.child("Sonnerie").child(k)
        val hopperUpdates: MutableMap<String, Any> = HashMap()
        hopperUpdates["ecoutee"] = true
        hopperRef.updateChildren(hopperUpdates)
        updateSonneriesRecues()
        addNotif(NotificationMusicMe.newInstanceSonnerieUtilisee(ringing, getCurrentUser()!!))
    }

    fun deleteSonneriePassee(ringing: Ringing) {
        database.getReference("Sonnerie").child(ringing.idSonnerie).removeValue()
    }

    private val sonnerieStateAddResult = MutableLiveData<AddFireBaseObjectResult>()
    fun getSonnerieStateAddResult(): MutableLiveData<AddFireBaseObjectResult> =
        sonnerieStateAddResult


    fun addSonnerieToUser(
        song: Song,
        contact: UserModel
    ) {

        val sonnerie = Ringing(
            song,
            Timestamp.now().seconds,
            false,
            contact.id,
            getCurrentUser()!!.id,
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
                addNotif(NotificationMusicMe.newInstanceEnvoieMusique(sonnerie, getCurrentUser()!!))
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

            _videoOneSearchResult.observe(lco, {
                if (it.error == null) {
                    val song = Song(
                        it.searchList[0].id,
                        it.searchList[0].title,
                        it.searchList[0].artworkUrl
                    )
                    val sonnerie = if (getCurrentUser() != null) {
                        Ringing(
                            song,
                            Timestamp.now().seconds,
                            false,
                            contact!!.id,
                            getCurrentUser()!!.id,
                            ""
                        )
                    } else {
                        Ringing(
                            song,
                            Timestamp.now().seconds,
                            false,
                            contact!!.id,
                            "",
                            senderName!!
                        )
                    }

                            val refSonnPush = referenceSon.push()
                            val keySonn = refSonnPush.key!!
                            sonnerie.idSonnerie = keySonn
                            refSonnPush.setValue(sonnerie).addOnCompleteListener { it2 ->
                                if (it2.isSuccessful) {
                                    favoriStateAddResult.value =
                                        AddFireBaseObjectResult(keySonn)
                                } else {
                                    favoriStateAddResult.value =
                                        AddFireBaseObjectResult(error = it2.exception)
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

        _videoOneSearchResult.observe(lco, {
            if (it.error == null) {
                val song = Song(
                    it.searchList[0].id,
                    it.searchList[0].title,
                    it.searchList[0].artworkUrl
                )
                val rootRef = database.reference.child("Song").child(song.id)
                rootRef.setValue(song).addOnCompleteListener { it2->
                    if (it2.isSuccessful) {
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
    private val listNotificationsLiveData = MutableLiveData<Map<String, NotificationMusicMe>>()
 //   private val notifVueLiveData = MutableLiveData<Boolean>()

    fun getNotifications(): LiveData<Map<String, NotificationMusicMe>> = listNotificationsLiveData
    //fun getNotifVue(): LiveData<Boolean> = notifVueLiveData


    fun notifAffichee() {
     //   notifVueLiveData.value = false
        val listNotifPasVues = listNotifications.filter { entry -> !entry.value.vue }
        for(n in listNotifPasVues) {
            val hopperRef: DatabaseReference = database.reference.child("Notification").child(n.key)
            val hopperUpdates: MutableMap<String, Any> = HashMap()
            hopperUpdates["vue"] = true
            hopperRef.updateChildren(hopperUpdates)
        }
        updateNotification()
    }

    private fun addNotif(notification: NotificationMusicMe) {
        val referenceNotif = database.getReference("Notification")
        val refNotifPush = referenceNotif.push()
        val e = refNotifPush.setValue(notification).exception
        if(e!=null)
        {
            Timber.e(e.toString())
        }


      /*  Timber.e("sendNotificationToUser ${notification.idReceiver}")
        sendNotificationToUser(notification.idReceiver, "Hi there puf!")


        val payload = JSONObject()

        try {
            //payload.put("sender", ParseInstallation.getCurrentInstallation().installationId)
            payload.put("sender", notification.sender!!.username)

            payload.put("notificationType", notification.type.name)
            payload.put("discussion", message.getDiscussion().objectId)
            val type = message.getDiscussion().getAssociatedFunctionality().name//message::class.java.simpleName.toString()
            payload.put("type", type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val data: HashMap<String, String?> = HashMap()
        data["messageData"] = payload.toString()



        ParseCloud.callFunctionInBackground<String>("notifMessage", data)*/
    }

    fun updateNotification() {
        val rootRef =
            database.reference.child("Notification")
        val query: Query =
            rootRef.orderByChild("idReceiver").equalTo(getCurrentUser()?.id!!)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listNotifications.clear()
                for (ds in p0.children) {
                    val notif = ds.getValue(NotificationMusicMe::class.java)!!
                    val referenceUser =
                        database.getReference("Users").child(notif.idReceiver)
                    referenceUser.addValueEventListener(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {}
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                listNotifications[ds.key!!] = notif
                                listNotificationsLiveData.value = listNotifications
                            }
                        })
                }
                if (!p0.hasChildren()) {
                    listNotificationsLiveData.value = listNotifications
                }
            }
        })
    }

    fun deleteNotif(notifKey: String) {
        Timber.e("delete")
        database.getReference("Notification").child(notifKey).removeValue()
        updateNotification()
    }


}