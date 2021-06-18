package com.vguivarc.wakemeup.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseActivity
import com.vguivarc.wakemeup.util.navigation.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.component.KoinComponent

private const val DEEPLINK_TOPIC_KEY = "tochange"

class MainActivity : BaseActivity(), KoinComponent {

    // live data to make sure it gets restored with activity configuration change (rotation...)
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } else {
            // Else, need to wait for onRestoreInstanceState
        }
        // showBottomNav(true)
        intent?.extras?.let { extras ->
            handleDeeplink(extras)
        }
    }

    private fun handleDeeplink(data: Bundle?) {
        data ?: return
        data.getString(DEEPLINK_TOPIC_KEY)?.toIntOrNull()?.let { serieId ->
/*            val action = HomeFragmentDirections.showSerie(serieId, null)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
            navHostFragment.navController.navigate(action)*/
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * The if/else check is useful when you want to listen to the onBackPressed() activity
     * method from a fragment. This check allows to trigger your callback on navigateUP() too.
     */
    override fun onSupportNavigateUp(): Boolean {
        return if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            onBackPressed()
            true
        } else
            currentNavController?.value?.navigateUp() ?: false
    }

    // -- PRIVATE FUNCTIONS -- //

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(
            R.navigation.home_clock,
            R.navigation.music,
            R.navigation.friends,
            R.navigation.history,
            R.navigation.settings
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        currentNavController = controller

        // Hide bottom nav for modal full screen fragments
        currentNavController?.observe(
            this,
            Observer {
                it.addOnDestinationChangedListener { _, destination, _ ->
                    if (destination.label in listOf(
                            "todo"
                        )
                    )
                        showBottomNav(false)
                    else
                        showBottomNav(true)
                }
            }
        )
    }

    @SuppressWarnings("MagicNumber")
    private fun showBottomNav(enableNav: Boolean) {
        bottom_nav
            .animate()
            .alpha(if (enableNav) 1f else 0f)
            .setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    bottom_nav.visibility = if (enableNav) View.VISIBLE else View.GONE
                }
            })
    }
}

/*
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
           AndroidApplication.repository.database.reference.child("LinkFirebaseAndFacebookIds")//.equalTo(currentUser?.uid!!, "appartientA")
        val query: Query =
            rootRef.orderByChild("IdFirebase")

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                AndroidApplication.repository.listeFriendStatic.clear()
                if (p0.childrenCount.toInt() > 0) {
                    for (ds in p0.children) {
                        val link = ds.getValue(LinkFirebaseAndFacebookIds::class.java)!!
                        AndroidApplication.repository.listeFriendStatic.add(link.idFacebook)
                    }
                }
            }
        })



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
                R.id.demanderMusique,
                R.id.rechercheVideoFragment,
                R.id.aPropos
                //R.id.settingsUser,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


/*      val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
       Timber.e("isLoggedIn=" + isLoggedIn)*/


        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isComplete){
                val firebaseToken = it.result.toString()
                Timber.i(firebaseToken)
            }
        }

            userMessageRegistration()

        val factory = ViewModelFactory(AndroidApplication.repository)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)

  checkFirstRun()



        this.configureNavigationView()

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

        val factory = ViewModelFactory(AndroidApplication.repository)
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
            updateProfilView()
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

    // Configure NavigationView

    private fun configureNavigationView() {
        updateProfilView()
    }

    private fun updateProfilView() {
        if (currentUser != null) {
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil).text = currentUser!!.username
            if (currentUser!!.imageUrl != "") {
                Glide.with(this)
                    .load(currentUser!!.imageUrl)
                    .into(navView.getHeaderView(0).findViewById(R.id.pictur_profil))
            } else {
                navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_logo))
            }
        } else {
            navView.getHeaderView(0).findViewById<TextView>(R.id.name_profil)
                .setText(R.string.utilisateur_anonyme)
            navView.getHeaderView(0).findViewById<ImageView>(R.id.pictur_profil)
                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_logo))
        }
    }

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
                // This is a new install (or the user cleared the shared preferences)
                intent = Intent(this, Demo::class.java)
                startActivity(intent)
            }
            currentVersionCode > savedVersionCode -> {
                //  This is an upgrade
                intent = Intent(this, Demo::class.java)
                startActivity(intent)
            }
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply()
    }
}*/
