package com.wakemeup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.wakemeup.connect.ConnectActivity

import com.wakemeup.connect.UserModel
import com.wakemeup.contact.ContactsListeFragment
import com.wakemeup.contact.SonnerieEnAttente
import com.wakemeup.reveil.ReveilsListeFragment
import com.wakemeup.song.Song
import com.wakemeup.share.DemanderMusique
import com.wakemeup.song.VideoFragment
import com.wakemeup.util.loadFavoris
import com.wakemeup.util.persisteFavoris
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    //FOR FRAGMENTS
    private var fragmentReveil: ReveilsListeFragment? = null
    private var fragmentMusique: VideoFragment? = null
    private var fragmentPartage : DemanderMusique? = null
    private var fragmentContact: ContactsListeFragment? = null
    private var fragmentParametre : SettingsUser? = null
    private var fragmentFavoris: VideoFragment? = null

    private var currentUser: UserModel? = null


    private fun updateMusiqueEnAttente() {
        AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.uid!!).child("receivedMusic")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (snapshot in p0.children) {
                            if (snapshot.key != null) {
                                val idMusicSend = snapshot.key!!
                                if (!AppWakeUp.listSonneriesEnAttente.containsKey(idMusicSend)) {
                                    val reveilRef = AppWakeUp.database.getReference("Reveils")
                                        .child((snapshot.getValue(String::class.java) as String))
                                    reveilRef.addValueEventListener(
                                        object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {}

                                            override fun onDataChange(p0: DataSnapshot) {
                                                val sender = ""
                                                var receiver = ""
                                                var song = ""
                                                for (snapshot2 in p0.children) {
                                                    if (snapshot2.key != null) {
                                                        when (snapshot2.key) {
                                                            "receiver" -> receiver =
                                                                snapshot2.value as String
                                                            "song" -> song =
                                                                snapshot2.value as String
                                                        }
                                                    }
                                                }
                                                val music =
                                                    SonnerieEnAttente(receiver, sender, song)
                                                AppWakeUp.addSonnerieEnAttente(
                                                    idMusicSend,
                                                    music,
                                                    this@MainActivity
                                                )
                                                Log.e(
                                                    "REVEIL MUSIC",
                                                    AppWakeUp.listSonneriesEnAttente.size.toString()
                                                )
                                                updateHotCount()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (AppWakeUp.auth.currentUser == null) {
            startConnectActivity()
        } else {
            Log.e("UserConnu", AppWakeUp.auth.currentUser!!.uid)
            Log.e("User anonyme : ", AppWakeUp.auth.currentUser!!.isAnonymous.toString())
            if (!(AppWakeUp.auth.currentUser!!.isAnonymous)) {
                updateListeAmis()
                updateMusiqueEnAttente()
            }

            // Configure all views
            this.configureToolBar()
            this.configureDrawerLayout()
            this.configureNavigationView()


            // Show First Fragment
            this.showFirstFragment()

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("MainActivity", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg =
                        "token notif : $token"//getString("Testouille token notif", token)
                    Log.e("MainActivity", msg)
                    Log.e("MainActivity", token!!)
                }
                )
        }
    }

    private lateinit var viewNbMusiquesEnAttente: TextView
    private lateinit var viewIconeMusiquesEnAttente: ImageView
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_reveils, menu)

        val alertMenuItem = menu.findItem(R.id.id_menu_bar_message)
        val rootView = alertMenuItem.actionView as RelativeLayout
        viewNbMusiquesEnAttente = rootView.findViewById(R.id.text_message_notification)
        viewIconeMusiquesEnAttente = rootView.findViewById(R.id.icone_musique_attente)
        if ((AppWakeUp.auth.currentUser!!.isAnonymous)) {

            viewNbMusiquesEnAttente.visibility = View.INVISIBLE
            viewIconeMusiquesEnAttente.visibility = View.INVISIBLE
        } else {
            updateHotCount()
        }

        rootView.setOnClickListener {

        }
        return super.onCreateOptionsMenu(menu)


    }


    private fun updateHotCount() {
        val newHotNumber = AppWakeUp.listSonneriesEnAttente.size
        var newNotif = false
        for (musicReceived in AppWakeUp.listSonneriesEnAttente.values) {
            if (musicReceived.notificationRecu) {
                newNotif = true
            }
        }
        if (newHotNumber == 0) {
            viewNbMusiquesEnAttente.visibility = View.INVISIBLE
            viewIconeMusiquesEnAttente.setImageResource(R.drawable.icon_music_no)
        } else {
            if (!newNotif) {
                viewNbMusiquesEnAttente.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_square_no)
            }
            viewIconeMusiquesEnAttente.setImageResource(R.drawable.icon_music_yes)
            viewNbMusiquesEnAttente.visibility = View.VISIBLE
            viewNbMusiquesEnAttente.text = newHotNumber.toString()

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
/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recycler_list_reveil.adapter?.notifyDataSetChanged()

        if(requestCode== ASK_CONNECTION_REQUEST_CODE){
            if(resultCode!= Activity.RESULT_OK ){
                // anonyme
                updateListeAmis()
            } else {
                //connecté
            }
        }
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        // Show fragment after user clicked on a menu item
        when (id) {
            R.id.activity_main_drawer_reveil -> this.showFragment(
                FragmentId.FRAGMENT_REVEIL
            )
            R.id.activity_main_drawer_musiques -> this.showFragment(
                FragmentId.FRAGMENT_MUSIQUES
            )
            R.id.activity_main_drawer_partage -> this.showFragment(
                FragmentId.FRAGMENT_PARTAGE
            )
            R.id.activity_main_drawer_amis -> this.showFragment(
                FragmentId.FRAGMENT_AMIS
            )
            R.id.activity_main_drawer_favoris -> this.showFragment(
                FragmentId.FRAGMENT_FAVORIS
            )
            R.id.activity_main_drawer_parametre -> this.showFragment(
                FragmentId.FRAGMENT_PARAMETRE
            )
            R.id.activity_main_drawer_deconnecter -> {
                AppWakeUp.auth.signOut()
                startConnectActivity()
            }
            R.id.activity_main_drawer_connecter -> {
                startConnectActivity(false)
            }

            else -> {
            }
        }

        this.drawerLayout!!.closeDrawer(GravityCompat.START)

        return true
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private fun configureToolBar() {
        this.toolbar = findViewById<View>(R.id.activity_main_toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    // Configure Drawer Layout
    private fun configureDrawerLayout() {
        this.drawerLayout = findViewById<View>(R.id.activity_main_drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,

            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()
    }

    // Configure NavigationView
    private fun configureNavigationView() {
        this.navigationView = findViewById<View>(R.id.activity_main_nav_view) as NavigationView
        navigationView!!.setNavigationItemSelectedListener(this)
        if (currentUser != null) {
            updateProfilView(currentUser!!)
        }
        //DataSnapshot
        if (!AppWakeUp.auth.currentUser!!.isAnonymous) {
            //updateProfilView(user)
            Log.e("test", AppWakeUp.auth.currentUser!!.uid)
            Log.e("test", AppWakeUp.auth.currentUser!!.isAnonymous.toString())
            val reference =
                AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

            reference.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                        currentUser = user
                        updateProfilView(user)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("MainActivity", "loadPost:onCancelled ${databaseError.message}")
                    }
                }
            )
            navigationView!!.menu.findItem(R.id.activity_main_drawer_favoris).isVisible = true
            navigationView!!.menu.findItem(R.id.activity_main_drawer_connecter).isVisible = false
            navigationView!!.menu.findItem(R.id.activity_main_drawer_deconnecter).isVisible = true
        } else {
            navigationView!!.menu.findItem(R.id.activity_main_drawer_favoris).isVisible = false
            navigationView!!.menu.findItem(R.id.activity_main_drawer_connecter).isVisible = true
            navigationView!!.menu.findItem(R.id.activity_main_drawer_deconnecter).isVisible = false
        }


//            auth.currentUser!!.displayName
    }

    private fun updateProfilView(user: UserModel) {
        //todo deuxième pas normal
        if (navigationView != null && navigationView!!.findViewById<TextView>(R.id.profil_name) != null) {
            navigationView!!.findViewById<TextView>(R.id.profil_name).text =
                user.username
            if (user.imageUrl == "default") {
                navigationView!!.findViewById<ImageView>(R.id.profil_picture)
                    .setImageResource(R.drawable.photo_profil)
            } else {
                Glide.with(this@MainActivity).load(
                    user.imageUrl
                ).into(navigationView!!.findViewById(R.id.profil_picture))
            }
        }
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private fun showFirstFragment() {
        val visibleFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_frame_layout)
        if (visibleFragment == null) {
            // Show News Fragment
            this.showFragment(FragmentId.FRAGMENT_REVEIL)
            // Mark as selected the menu item corresponding to VideoFragment
            this.navigationView!!.menu.getItem(0).isChecked = true
        }
    }

    // Show fragment according an Identifier

    private fun showFragment(fragmentIdentifier: FragmentId) {
        when (fragmentIdentifier) {
            FragmentId.FRAGMENT_SECONNECTER -> this.showSeConnecterFragment()
            FragmentId.FRAGMENT_SEDECONNECTER -> this.showSeDeconnecterFragment()
            FragmentId.FRAGMENT_MUSIQUES -> this.showMusiquesFragment()
            FragmentId.FRAGMENT_PARTAGE -> this.showPartageFragment()
            FragmentId.FRAGMENT_AMIS -> this.showAmisFragment()
            FragmentId.FRAGMENT_REVEIL -> this.showReveilFragment()

            FragmentId.FRAGMENT_FAVORIS -> this.showFavorisFragment()

            FragmentId.FRAGMENT_PARAMETRE -> this.showParametreFragment()
            else -> {
            }
        }
    }



    // ---

    // Create each fragment page and show it

    private fun showFavorisFragment() {
        //TODO Créer le fragment des favoris
    }

    private fun showReveilFragment() {
        if (this.fragmentReveil == null) {
            this.fragmentReveil = ReveilsListeFragment.newInstance()
        }
        this.startTransactionFragment(this.fragmentReveil!!)
    }

    private fun showAmisFragment() {
        if (this.fragmentContact == null) {
            this.fragmentContact = ContactsListeFragment.newInstance()
        }
        this.startTransactionFragment(this.fragmentContact!!)
    }

    private fun showMusiquesFragment() {
        if (this.fragmentMusique == null) {
            this.fragmentMusique = VideoFragment.newInstance(this)
        }
        this.startTransactionFragment(this.fragmentMusique!!)

    }

    private fun showPartageFragment() {
        if (this.fragmentPartage == null) {
            this.fragmentPartage = DemanderMusique.newInstance(this)
        }
        supportFragmentManager.beginTransaction().remove(this.fragmentPartage!!).commit()
        this.startTransactionFragment(this.fragmentPartage!!)
    }

    private fun showParametreFragment() {
        if (this.fragmentParametre == null) {
            this.fragmentParametre= SettingsUser.newInstance(this)
        }
        this.startTransactionFragment(this.fragmentParametre!!)
    }

    private fun showSeConnecterFragment() {
        if (this.fragmentMusique == null) {
            this.fragmentMusique = VideoFragment.newInstance(this)
        }
        this.startTransactionFragment(this.fragmentMusique!!)
    }

    private fun showSeDeconnecterFragment() {
        if (this.fragmentMusique == null) {
            this.fragmentMusique = VideoFragment.newInstance(this)
        }
        this.startTransactionFragment(this.fragmentMusique!!)

    }

    // ---

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private fun startTransactionFragment(fragment: Fragment) {
        if (!fragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame_layout, fragment).commit()
        }
    }

    public fun persisteFavorisMain(currentSong: Song?) {
        persisteFavoris(this,currentSong)
    }

    public fun loadFavorisMain() : Song {
        return loadFavoris(this)
    }


    companion object {

        enum class FragmentId {
            FRAGMENT_REVEIL,
            FRAGMENT_MUSIQUES,
            FRAGMENT_PARTAGE,
            FRAGMENT_AMIS,
            FRAGMENT_SECONNECTER,
            FRAGMENT_SEDECONNECTER,
            FRAGMENT_FAVORIS,
            FRAGMENT_PARAMETRE

        }
    }


    private fun updateListeAmis() {
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

        AppWakeUp.database.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snap in dataSnapshot.children) {
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
    }

    fun startConnectActivity(clear: Boolean = true) {
        val intent = Intent(this@MainActivity, ConnectActivity::class.java)
        if (clear) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }


}