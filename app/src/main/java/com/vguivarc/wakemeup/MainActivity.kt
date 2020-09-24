package com.vguivarc.wakemeup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vguivarc.wakemeup.connect.LinkFirebaseAndFacebookIds
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.notification.NotifListeViewModel
import com.vguivarc.wakemeup.notification.NotificationMusicMe
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.sonnerie.Sonnerie
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    companion object{
        const val PREFS_NAME = "MyPrefsFile"
        const val PREF_VERSION_CODE_KEY = "version_code"
        const val DOESNT_EXIST = -1
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicme_app_main)


        val rootRef =
           AppWakeUp.repository.database.reference.child("LinkFirebaseAndFacebookIds")//.equalTo(currentUser?.uid!!, "appartientA")
        val query: Query =
            rootRef.orderByChild("IdFirebase")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                AppWakeUp.repository.listeFriendStatic.clear()
                if (p0.childrenCount.toInt() > 0) {
                    for (ds in p0.children) {
                        val link = ds.getValue(LinkFirebaseAndFacebookIds::class.java)!!
                        AppWakeUp.repository.listeFriendStatic.add(link.idFacebook)
                    }
                }
            }
        })


    /*    try {
            val info = packageManager.getPackageInfo(
                "com.vguivarc.wakemeup",  //Insert your own package name.
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Timber.e("KeyHash:"+ Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                "com.vguivarc.wakemeup",  //Insert your own package name.
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val v = Base64.encodeToString(md.digest(), Base64.DEFAULT)

                Timber.e("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
*/


        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.navHostFragment)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navView = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.profilFragment,
                R.id.reveilsListeFragment,
                R.id.favorisFragment,
                R.id.musiquesRecuesFragment,
                R.id.contactsListeFragment,
                R.id.demanderMusique
                //R.id.settingsUser,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


/*      val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
       Timber.e("isLoggedIn=" + isLoggedIn)*/



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
        viewNbMusiquesEnAttente = rootViewReveil.findViewById(R.id.text_message_reveil)
        viewIconeMusiquesEnAttente = rootViewReveil.findViewById(R.id.icone_musique_attente)
        rootViewReveil.setOnClickListener {
            navController.navigate(
                R.id.musiquesRecuesFragment
            )
        }


        val rootViewNotif = notifMenuItem.actionView as RelativeLayout
        viewNbNotifEnAttente = rootViewNotif.findViewById(R.id.text_message_notification)
        viewIconeNotifEnAttente = rootViewNotif.findViewById(R.id.icone_notif_attente)
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

        viewModelSonnerie.getListeAttenteLiveData().observe(this, {
            updateSonnerieHotCountNumber(it)
        })
        viewModelSonnerie.getListeVueLiveData().observe(this, {
            updateHotCountSonnerieActivated(it)
        })


        viewModelNotif.getNotifLiveData().observe(this, {
            updateNotifHotCountNumber(it)
        })

        currentUserViewModel.getCurrentUserLiveData().observe(this, {
            currentUser = it
            if (currentUser == null) {
                viewNbMusiquesEnAttente.visibility = View.INVISIBLE
                viewIconeMusiquesEnAttente.visibility = View.INVISIBLE
                viewNbNotifEnAttente.visibility = View.INVISIBLE
                viewIconeNotifEnAttente.visibility = View.INVISIBLE

            } else {
                val extrasToDealWith = intent.extras
                dealWithExtra(extrasToDealWith)
                intent.replaceExtras(null)
                viewIconeMusiquesEnAttente.visibility = View.VISIBLE
                viewIconeNotifEnAttente.visibility = View.VISIBLE
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
        val newHotNumber = listSonnerieAttente.filter { entry -> !entry.value.vue }.size
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

   /* private fun updateProfilView() {
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
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil).text = currentUser!!.username
            if (currentUser!!.imageUrl != "") {
                Glide.with(this)
                    .load(currentUser!!.imageUrl)
                    .into(navView.getHeaderView(0).findViewById(R.id.pictur_profil))
            } else {
                navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.photo_profil))
            }
        } else {
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil)
                .setText(R.string.utilisateur_anonyme)
            navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.empty_picture_profil))
        }
    }*/*/

    private fun dealWithExtra(extrasToDealWith: Bundle?) {
        if (extrasToDealWith != null) {
            val favyt = extrasToDealWith.getString("favyt")
            if (favyt != null) {
                if (currentUser != null) {
                    currentUserViewModel.addFavoriString(this, favyt)
                    navController.navigate(R.id.favorisFragment, extrasToDealWith)
                }
            }
        }
    }


    private fun checkFirstRun() {

        // Get current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs: SharedPreferences =
            getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedVersionCode: Int = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST)

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode -> {
                // This is just a normal run
                return
            }
            savedVersionCode == DOESNT_EXIST -> {
                // TODO This is a new install (or the user cleared the shared preferences)
                intent = Intent(this, Demo::class.java)
                startActivity(intent)
            }
            currentVersionCode > savedVersionCode -> {
                // TODO This is an upgrade
                intent = Intent(this, Demo::class.java)
                startActivity(intent)
            }
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }
}
