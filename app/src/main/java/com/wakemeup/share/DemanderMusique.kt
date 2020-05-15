package com.wakemeup.share

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.UserModel
import java.sql.Timestamp

private lateinit var username: String
private lateinit var context: Context
class DemanderMusique : Fragment() {

    interface ShareListener {
        fun onClickShare()
    }

    var listener: ShareListener? = null

    companion object {
        fun newInstance(ctx: Context): DemanderMusique {
            context = ctx
            return DemanderMusique()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //on récupére l'user pour l'username
        val referenceUsername =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        referenceUsername.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    username = user.username

                    //création de la demande de musique
                    val reference = AppWakeUp.database.getReference("DemandeMusique")
                    val currentuser = AppWakeUp.auth.currentUser!!
                    val demande = DemandeMusique(
                        currentuser.uid,
                        username,
                        Timestamp(System.currentTimeMillis())
                    )
                    var refpush = reference.push()
                    var key = refpush.key!!
                    refpush.setValue(demande).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("DemanderMusique", "demande ajoutée")
                        } else {
                            Log.i("DemanderMusique", "erreur demande")
                        }
                    }
                    var lien = "wakemeup.fr/demande/" + key

                    //liste des intents pour chaque application
                    val applicationCible: MutableList<Parcelable> = ArrayList()
                    val intentDebut = Intent()
                    intentDebut.action = Intent.ACTION_SEND
                    intentDebut.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.message_partage_general)
                    )
                    applicationCible.add(intentDebut)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    val resInfos =
                        requireContext().packageManager.queryIntentActivities(shareIntent, 0)
                    if (!resInfos.isEmpty()) {
                        for (i in 0..resInfos.size - 1) {
                            val appli: ResolveInfo = resInfos.get(i)
                            val packageName = appli.activityInfo.packageName

                            if (packageName.contains("com.google.android.gm")) {
                                val intent = Intent()
                                intent.component =
                                    ComponentName(packageName, appli.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte gmail : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("com.twitter.android")) {
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte twitter  : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("mms")) {
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte sms : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("com.instagram.android")) {
                                val intent = Intent()
                                intent.component =
                                    ComponentName(packageName, appli.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte instagram : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("snapchat")) {
                                val intent = Intent()
                                intent.component =
                                    ComponentName(packageName, appli.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte snapchat : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("facebook")) {
                                val intent = Intent()
                                intent.component =
                                    ComponentName(packageName, appli.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("discord")) {
                                val intent = Intent()
                                intent.component =
                                    ComponentName(packageName, appli.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte discord : " + lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                        }

                        //on vérifie la présence d'application
                        if (!applicationCible.isEmpty()) {
                            val intentVide = Intent()

                            //tableau parcelable qui contiendra les intents des applications
                            val arrayIntent = Array<Intent>(applicationCible.size) { intentVide }

                            val chooserIntent = Intent.createChooser(
                                applicationCible.removeAt(0) as Intent,
                                "choix application"
                            )
                            for (j in 0..applicationCible.size - 1) {
                                arrayIntent[j] = applicationCible.get(j) as Intent
                            }
                            chooserIntent.putExtra(
                                Intent.EXTRA_ALTERNATE_INTENTS, arrayIntent
                            )
                            startActivity(chooserIntent)
                        } else {
                            Toast.makeText(context, "Pas d'application", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })


    }
}