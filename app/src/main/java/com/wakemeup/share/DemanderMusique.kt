package com.wakemeup.share

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.neocampus.repo.ViewModelFactory
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.contact.ContactsListeFragment
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

        var twitter = false
        val applicationCible: MutableList<Parcelable> = ArrayList()
        val intentDebut = Intent()
        intentDebut.action = Intent.ACTION_SEND
        intentDebut.putExtra(Intent.EXTRA_TEXT, "Partager sur wakemeup")
        applicationCible.add(intentDebut)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        val resInfos =context!!.packageManager.queryIntentActivities(shareIntent, 0)
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
                    intent.putExtra(Intent.EXTRA_TEXT, "Text Gmail")
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
                        intent.putExtra(Intent.EXTRA_TEXT, "Text twitter")
                        intent.setPackage(packageName)
                        applicationCible.add(intent)
                    }
                }
                if (packageName.contains("mms")) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "Text sms")
                    intent.setPackage(packageName)
                    applicationCible.add(intent)
                }
                if (packageName.contains("com.instagram.android")) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "Text instagram")
                    intent.setPackage(packageName)
                    applicationCible.add(intent)
                }
                if (packageName.contains("snapchat")) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "Text snapchat")
                    intent.setPackage(packageName)
                    applicationCible.add(intent)
                }
                if (packageName.contains("facebook")) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "www.google.com")
                    intent.setPackage(packageName)
                    applicationCible.add(intent)
                }
                if (packageName.contains("discord")) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, "Partage sur discord ")
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

}