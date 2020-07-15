package com.vguivarc.wakemeup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.vguivarc.wakemeup.connect.ConnectActivity
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.contact.ContactsListeFragmentDirections
import com.vguivarc.wakemeup.notification.NotifListeViewModel
import com.vguivarc.wakemeup.notification.NotificationMusicMe
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.share.LienMusicMe
import com.vguivarc.wakemeup.sonnerie.Sonnerie
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility
import timber.log.Timber
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private var drawerLayout: DrawerLayout? = null
    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var viewModelNotif: NotifListeViewModel


    private var currentUser: UserModel? = null
    private lateinit var currentUserViewModel: CurrentUserViewModel


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private val REQUEST_IMAGE_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicme_app_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.navHostFragment)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navView = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.reveilsListeFragment,
                R.id.favorisFragment,
                R.id.musiquesRecuesFragment,
                R.id.contactsListeFragment,
                R.id.demanderMusique,
                //R.id.settingsUser,
                R.id.activity_main_drawer_deconnecter
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.tag("MainActivity").w(task.exception, "getInstanceId failed")
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                val msg =
                    "token notif : $token"//getString("Testouille token notif", token)
                Timber.e(msg)
                Timber.e(token!!)
            }
            )


        val factory = ViewModelFactory(AppWakeUp.repository)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)

        checkFirstRun()
        this.configureNavigationView()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //val extras = data!!.extras
             //   val imageBitmap = extras!!["data"] as Bitmap?
               // uploadImageAndSaveUri(imageBitmap!!)



                 val extras = data!!.extras
                if (extras != null) {
                    val newProfilePic = extras.getParcelable<Bitmap>("data")
                    uploadImageAndSaveUri(newProfilePic!!)
                }
            }
        }
    }

    private lateinit var imageUri: Uri

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${currentUser!!.id}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        //progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            //  progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                            .setImageBitmap(bitmap)
                        val reference = AppWakeUp.repository.database.getReference("Users")
                            .child(currentUser!!.id)
                        val updatedUser =
                            UserModel(currentUser!!.id, imageUri.toString(), currentUser!!.username)
                        reference.setValue(updatedUser)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    Utility.createSimpleToast(it.toString())
                }
            }
        }

    }

    private lateinit var viewNbMusiquesEnAttente: TextView
    private lateinit var viewIconeMusiquesEnAttente: ImageView
    private lateinit var viewNbNotifEnAttente: TextView
    private lateinit var viewIconeNotifEnAttente: ImageView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_reveils, menu)

        val alertMenuItem = menu.findItem(R.id.id_menu_bar_message)
        val notifMenuItem = menu.findItem(R.id.id_menu_bar_notif)

        val rootViewReveil = alertMenuItem.actionView as RelativeLayout
        viewNbMusiquesEnAttente = rootViewReveil.findViewById<TextView>(R.id.text_message_reveil)
        viewIconeMusiquesEnAttente = rootViewReveil.findViewById<ImageView>(R.id.icone_musique_attente)
        rootViewReveil.setOnClickListener {
            navController.navigate(
                R.id.musiquesRecuesFragment
            )
        }


        val rootViewNotif = notifMenuItem.actionView as RelativeLayout
        viewNbNotifEnAttente = rootViewNotif.findViewById<TextView>(R.id.text_message_notification)
        viewIconeNotifEnAttente = rootViewNotif.findViewById<ImageView>(R.id.icone_notif_attente)
        rootViewNotif.setOnClickListener {
            navController.navigate(
                R.id.notifsListeFragment
            )
        }

        val factory = ViewModelFactory(AppWakeUp.repository)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)
        viewModelSonnerie =
            ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)
        viewModelNotif =
            ViewModelProvider(this, factory).get(NotifListeViewModel::class.java)

        viewModelSonnerie.getListeAttenteLiveData().observe(this, androidx.lifecycle.Observer {
            updateSonnerieHotCountNumber(it)
        })
        viewModelSonnerie.getListeVueLiveData().observe(this, androidx.lifecycle.Observer {
            updateHotCountSonnerieActivated(it)
        })


        viewModelNotif.getNotifLiveData().observe(this, androidx.lifecycle.Observer {
            updateNotifHotCountNumber(it)
        })

        currentUserViewModel.getCurrentUserLiveData().observe(this, androidx.lifecycle.Observer {
            currentUser=it
            if(currentUser==null){
                viewNbMusiquesEnAttente.visibility = View.INVISIBLE
                viewIconeMusiquesEnAttente.visibility = View.INVISIBLE
                viewNbNotifEnAttente.visibility = View.INVISIBLE
                viewIconeNotifEnAttente.visibility = View.INVISIBLE
                navView.menu.findItem(R.id.activity_main_drawer_connecter).isVisible = true
                navView.menu.findItem(R.id.activity_main_drawer_deconnecter).isVisible = false
            } else {
                updateProfilView()
                val extrasToDealWith= intent.extras
                dealWithExtra(extrasToDealWith)
                intent.replaceExtras(null)
                viewIconeMusiquesEnAttente.visibility = View.VISIBLE
                viewIconeNotifEnAttente.visibility = View.VISIBLE
                navView.menu.findItem(R.id.activity_main_drawer_connecter).isVisible = false
                navView.menu.findItem(R.id.activity_main_drawer_deconnecter).isVisible = true
            }
        })
            // viewModelSonnerie.updateSonneries()
           // viewModelSonnerie.updateSonneriesEnvoyees()
        return super.onCreateOptionsMenu(menu)

    }

    private fun updateHotCountSonnerieActivated(newNotif: Boolean) {
        if (!newNotif && viewNbMusiquesEnAttente.visibility==View.VISIBLE) {
            viewNbMusiquesEnAttente.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_square_blue)
        } else {
            viewNbMusiquesEnAttente.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_square)
        }
    }

    private fun updateSonnerieHotCountNumber(listSonnerieAttente: Map<String, Sonnerie>) {
        val newHotNumber = listSonnerieAttente.size
        if (newHotNumber == 0) {
            viewNbMusiquesEnAttente.visibility = View.INVISIBLE
            viewIconeMusiquesEnAttente.setImageResource(R.drawable.icon_music_no)
            viewNbMusiquesEnAttente.text = null
            viewModelSonnerie.listeAffichee()
        } else {
            viewIconeMusiquesEnAttente.setImageResource(R.drawable.icon_music_yes)
            viewNbMusiquesEnAttente.visibility = View.VISIBLE
            viewNbMusiquesEnAttente.text = newHotNumber.toString()
        }
    }

    private fun updateNotifHotCountNumber(listSonnerieAttente: Map<String, NotificationMusicMe>) {
        val newHotNumber = listSonnerieAttente.filter { entry -> entry.value.vue == false }.size
        if (newHotNumber == 0) {
            viewNbNotifEnAttente.visibility = View.INVISIBLE
            viewIconeNotifEnAttente.setImageResource(R.drawable.icon_notif_no)
            viewNbNotifEnAttente.text = null
        } else {
            viewIconeNotifEnAttente.setImageResource(R.drawable.icon_notif_yes)
            viewNbNotifEnAttente.visibility = View.VISIBLE
            viewNbNotifEnAttente.text = newHotNumber.toString()
            viewNbNotifEnAttente.background =
                ContextCompat.getDrawable(this, R.drawable.rounded_square)
        }
    }


    override fun onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Configure NavigationView

    private fun configureNavigationView() {
        updateProfilView()

        //DataSnapshot
        navView.menu.findItem(R.id.activity_main_drawer_connecter).setOnMenuItemClickListener {
            connect(true)
            true
        }
        navView.menu.findItem(R.id.activity_main_drawer_deconnecter).setOnMenuItemClickListener {
            disconnect()
            true
        }


//            auth.currentUser!!.displayName
    }

    private fun updateProfilView() {
        if (currentUser != null) {
            navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                .setOnClickListener {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
                        pictureIntent.resolveActivity(this.packageManager)?.also {
                            val intent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI
                            )
                            intent.type = "image/*"
                            intent.putExtra("crop", "true")
                            intent.putExtra("scale", true)
                            intent.putExtra("outputX", 256)
                            intent.putExtra("outputY", 256)
                            intent.putExtra("aspectX", 1)
                            intent.putExtra("aspectY", 1)
                            intent.putExtra("return-data", true)
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil)
                .setText(currentUser!!.username)
            if (currentUser!!.imageUrl != "") {
                Glide.with(this)
                    .load(currentUser!!.imageUrl)
                    .into(navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil))
            } else {
                navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.photo_profil))
            }
        } else {
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil)
                .setText("Utilisateur anonyme")
            navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.empty_picture_profil))
        }
    }


    /*private fun updateListeAmis() {
        //TODO PARSE


        val contactsPhone = mutableMapOf<String, String>()
        val contactsApp = mutableListOf<UserModel>()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 2)
        }

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )

        if (cursor == null) {
            Log.e("AmisFragment", "cursor == null")
        } else {

            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE))
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)) == "1") {
                    var phone =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    phone = phone.replace("+33", "0")
                    phone = phone.replace(" ", "")
                    contactsPhone[phone] = name
                }
            }
            cursor.close()
        }

        AppWakeUp.repository.database.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snap in dataSnapshot.children) {
                        Log.i("MainActivity", snap.toString())
                        val user: UserModel = snap.getValue(UserModel::class.java)!!
                        if (contactsPhone.containsKey(user.phone) && user.id != AppWakeUp.auth.uid) {
                            contactsApp.add(user)
                        }
                    }
                    AppWakeUp.listeAmis = contactsApp
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                }
            }
        )
    }*/

    fun connect(clear: Boolean = true) {
        Timber.e("A")
        val intent = Intent(this@MainActivity, ConnectActivity::class.java)
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        Timber.e("B")
        startActivity(intent)
    }

    fun disconnect(clear: Boolean = true) {
        currentUserViewModel.disconnect()
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }


    fun dealWithExtra(extrasToDealWith: Bundle?) {
        if (extrasToDealWith != null) {
            val favyt = extrasToDealWith.getString("favyt")
            if (favyt != null) {
                if (currentUser != null) {
                    currentUserViewModel.addFavoriString(this, favyt)
                    navController.navigate(R.id.favorisFragment, extrasToDealWith)
                }
            }

            val link = extrasToDealWith.getString("link")
            if (link != null) {
                val reference =
                    AppWakeUp.repository.database.getReference("LienMusicMe").child(link)
                reference.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val lien = dataSnapshot.getValue(LienMusicMe::class.java)!!
                            if (currentUser == null || lien.userID != currentUser!!.id) {
                                val reference2 =
                                    AppWakeUp.repository.database.getReference("Users")
                                        .child(lien.userID)
                                reference2.addValueEventListener(
                                    object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val contact =
                                                dataSnapshot.getValue(UserModel::class.java)!!
                                            val action =
                                                ContactsListeFragmentDirections.actionContactsListeFragmentToContactFragment(
                                                    contact
                                                )
                                            extrasToDealWith!!.putParcelable("contact", contact)
                                            navController.navigate(
                                                R.id.contactFragment,
                                                extrasToDealWith
                                            )

                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            Timber.e(
                                                "Link:getUser ${databaseError.message}"
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Timber.e("Link:getLink ${databaseError.message}")
                        }
                    }
                )
            }
        }
    }


    private fun checkFirstRun() {
        val PREFS_NAME = "MyPrefsFile"
        val PREF_VERSION_CODE_KEY = "version_code"
        val DOESNT_EXIST = -1

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersionCode: Int = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            return
        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)
            intent = Intent(this, Demo::class.java)
            startActivity(intent)
        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
            intent = Intent(this, Demo::class.java)
            startActivity(intent)
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }
}
