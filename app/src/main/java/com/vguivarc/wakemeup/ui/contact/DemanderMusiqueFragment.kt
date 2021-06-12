package com.vguivarc.wakemeup.ui.contact

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.LienMusicMe
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import timber.log.Timber
import java.sql.Timestamp

//TODO refaire ça : le lien donne sur le site mais peut être ouvert dans l'appli (ça génère un truc partageable en mode Facebook ?)
//TODO ajouter le fait de demander par notif ?
class DemanderMusiqueFragment : Fragment() {
    private lateinit var username: String

    private var  currentUser : UserModel? = null
    private lateinit var currentUserViewModel: CurrentUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_partage, container, false)
        setHasOptionsMenu(false)

        val factory = ViewModelFactory(AndroidApplication.repository)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)

        currentUserViewModel.getCurrentUserLiveData().observe(requireActivity(), {
            currentUser=it
            if (currentUser==null) {
                view.findViewById<TextView>(R.id.id_partage_text_pas_connecte).visibility= View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.id_partage_ensemble_connecte).visibility= View.GONE
            } else {
                view.findViewById<TextView>(R.id.id_partage_text_pas_connecte).visibility= View.GONE
                view.findViewById<ConstraintLayout>(R.id.id_partage_ensemble_connecte).visibility= View.VISIBLE
            }
        })
        view.findViewById<ImageButton>(R.id.id_partage_bouton).setOnClickListener{
            partager()
        }

        return view
    }


//TODO ajouter "partager" avec MusicMe
       private fun partager() {

    if(currentUser!=null) {
        //on récupére l'user pour l'username
        val referenceUsername =
            AndroidApplication.repository.database.getReference("Users").child(currentUser!!.id)

        referenceUsername.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    username = user.username
/*
                    //création de la demande de musique
                    val reference = AndroidApplication.repository.database.getReference("LienMusicMe")
                    val demande = LienMusicMe(
                        currentUser!!.id,
                        username,
                        Timestamp(System.currentTimeMillis()).time
                    )
                    val refpush = reference.push()
                    val key = refpush.key!!
                    refpush.setValue(demande).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Timber.i("lien ajouté")
                        } else {
                            Timber.i("erreur lien")
                        }
                    }
                    val lien = "https://lesseptrois.freeboxos.fr/demande?id=$key"

                    //liste des intents pour chaque application
                    val applicationCible: MutableList<Parcelable> = ArrayList()
                    val intentDebut = Intent()
                    intentDebut.action = Intent.ACTION_SEND
                    intentDebut.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.message_partage_general) + "\n" + lien
                    )
                    applicationCible.add(intentDebut)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    val resInfos =
                        requireContext().packageManager.queryIntentActivities(shareIntent, 0)
                    if (resInfos.isNotEmpty()) {
                        for (i in 0 until resInfos.size) {
                            val appli: ResolveInfo = resInfos[i]
                            val packageName = appli.activityInfo.packageName

                            when {
                                packageName.contains("com.google.android.gm") -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("com.twitter.android") -> {
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_twitter) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("mms") -> {
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("sms") -> {
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("com.instagram.android") -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("snapchat") -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("facebook") -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_facebook) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                packageName.contains("discord") -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                                else -> {
                                    val intent = Intent()
                                    intent.component =
                                        ComponentName(packageName, appli.activityInfo.name)
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.message_partage_general) + "\n" + lien
                                    )
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                            }
                        }

                        //on vérifie la présence d'application
                        if (applicationCible.isNotEmpty()) {
                            val intentVide = Intent()

                            //tableau parcelable qui contiendra les intents des applications
                            val arrayIntent = Array(applicationCible.size) { intentVide }

                            val chooserIntent = Intent.createChooser(
                                applicationCible.removeAt(0) as Intent,
                                "choix application"
                            )
                            for (j in 0 until applicationCible.size) {
                                arrayIntent[j] = applicationCible[j] as Intent
                            }
                            chooserIntent.putExtra(
                                Intent.EXTRA_ALTERNATE_INTENTS, arrayIntent
                            )
                            startActivity(chooserIntent)
                        } else {
                            Toast.makeText(context, "Pas d'application", Toast.LENGTH_SHORT).show()
                        }
                    }*/
                }
            })

    }

    }
}