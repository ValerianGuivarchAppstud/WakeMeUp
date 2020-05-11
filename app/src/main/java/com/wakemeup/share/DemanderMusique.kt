
package com.wakemeup.share

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.neocampus.repo.ViewModelFactory
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.UserModel
import com.wakemeup.contact.ContactsListeFragment
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
private lateinit var username: String
private lateinit var context: Context
class DemanderMusique : Fragment(){

    interface  ShareListener{
        fun onClickShare()
    }

    var listener : ShareListener? = null
    companion object {
        fun newInstance(ctx : Context): DemanderMusique {
            context = ctx
            return DemanderMusique()
        }
    }

  /*  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_share, container, false)

        return view

    }*/



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val referenceUsername =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        referenceUsername.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    username = user.username
                    val reference =  AppWakeUp.database.getReference("DemandeMusique")
                    val currentuser = AppWakeUp.auth.currentUser!!
                    val demande = DemandeMusique(currentuser.uid, username, Timestamp(System.currentTimeMillis()))
                    var refpush = reference.push()
                    var key = refpush.key!!
                    refpush.setValue(demande).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("DemanderMusique","demande ajoutée")
                            Log.i("DemanderMusique", key)
                        } else {
                            Log.i("DemanderMusique","erreur demande ajoutée")
                        }
                    }
                    var lien = "wakemeup.fr/demande/"+key
                    var twitter = false
                    val applicationCible: MutableList<Parcelable> = ArrayList()
                    val intentDebut = Intent()
                    intentDebut.action = Intent.ACTION_SEND
                    intentDebut.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_partage_general))
                    applicationCible.add(intentDebut)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    val resInfos =requireContext().packageManager.queryIntentActivities(shareIntent, 0)
                    if (!resInfos.isEmpty()) {
                        for (i in 0..resInfos.size - 1) {
                            val resInfo: ResolveInfo = resInfos.get(i)
                            val packageName = resInfo.activityInfo.packageName
                            Log.w("MainActivity", packageName)
                            if (packageName.contains("com.google.android.gm")) {
                                val intent = Intent()
                                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte gmail : "+lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            //test booleen car il a 2 version de twitter
                            if (twitter == false) {
                                if (packageName.contains("com.twitter.android")) {
                                    twitter = true
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_TEXT, "Texte twitter  : "+lien)
                                    intent.setPackage(packageName)
                                    applicationCible.add(intent)
                                }
                            }
                            if (packageName.contains("mms")) {
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte sms : "+lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("com.instagram.android")) {
                                val intent = Intent()
                                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte instagram : "+lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("snapchat")) {
                                val intent = Intent()
                                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte snapchat : "+lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("facebook")) {
                                val intent = Intent()
                                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, lien)
                                intent.setPackage(packageName)
                                applicationCible.add(intent)
                            }
                            if (packageName.contains("discord")) {
                                val intent = Intent()
                                intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                                intent.action = Intent.ACTION_SEND
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, "Texte discord : "+lien)
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
                            for (j in 0..applicationCible.size-1) {
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



    public fun getUsername() {

        val reference =
            AppWakeUp.database.getReference("Users").child(AppWakeUp.auth.currentUser!!.uid)

        reference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    username = user.username
                    Log.i("usernameLiveData",username)

                }
            })
        Log.i("username2",username)
        Log.i("username","onepassela")

    }
}





/*
package com.wakemeup.share

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Parcel
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ApplicationSelectorReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("selector","okok")
        for( key in Objects.requireNonNull(intent!!.extras)!!.keySet()){
            val componentInfo = ComponentName(intent.extras!!.get(key) as Parcel?)
            val packageManager = context!!.packageManager
            val appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentInfo.packageName, PackageManager.GET_META_DATA))
            Log.i("Selector",appName as String)
        }
    }

}

 */










