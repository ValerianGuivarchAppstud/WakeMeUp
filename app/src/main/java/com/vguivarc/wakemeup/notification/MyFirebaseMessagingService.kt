package com.vguivarc.wakemeup.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.ui.MainActivity
import com.vguivarc.wakemeup.util.Utility
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.e("From: ${remoteMessage.from}")


        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            /*if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {*/
            // Handle message within 10 seconds
            Timber.e("Data: ${remoteMessage.data}")
            Timber.e("Sender: ${remoteMessage.data.get("sender")}")
            Timber.e("Type: ${remoteMessage.data.get("notificationType")}")
            handleNow(remoteMessage.data)
            //}
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.e("Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.e("Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
           sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        if(token!=null)
        FirebaseMessaging.getInstance().subscribeToTopic(token)
            .addOnCompleteListener { task ->
                Timber.e("token registration : ${task.isSuccessful}")
            }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow(data: MutableMap<String, String>) {

        val sender=data.get("sender")!!
        val notificationType=data.get("notificationType")!!
        val urlPicture = data.get("urlPicture")!!
        val usernameSender = data.get("usernameSender")!!
        //val json = JSONObject(data["data"]!!)
        //val sender = data.values.toList()[0]//sender
        //val notificationType = data.values.toList()[1] //notificationType
        //if (AnnonceMessagerieFragment.currentDisplayedDiscussionId != discussionId)
            notificationAnnonce(sender, notificationType, urlPicture, usernameSender)
    }

    var notificationChannel: NotificationChannel? =null
    lateinit var builder: Notification
    val channelId = "com.vguivarc.wakemeup.notification"
    val description = "Nouveau(s) message(s)"



    private fun notificationAnnonce(
        sender: String,
        notificationType: String,
        urlPicture: String,
        usernameSender: String
    ) {
        val mainActivityIntent = Intent(this.application, MainActivity::class.java)

        //mainActivityIntent.putExtra("notificationId", notificationId)
        mainActivityIntent.putExtra("notificationType", notificationType)
        mainActivityIntent.data = (Uri.parse("foobar://" + SystemClock.elapsedRealtime()))
        val pi = PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, 0)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManage = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val texte = when(notificationType){
            NotificationMusicMe.NotificationType.ENVOIE_MUSIQUE.name -> AndroidApplication.appContext.resources.getString(R.string.envoie_musique, usernameSender)
            NotificationMusicMe.NotificationType.SONNERIE_UTILISEE.name -> AndroidApplication.appContext.resources.getString(R.string.sonnerie_utilisee, usernameSender)
            else -> ""
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationChannel==null) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_LOW)
                notificationChannel!!.enableLights(true)
                notificationChannel!!.lightColor = Color.BLUE
                notificationChannel!!.enableVibration(true)
                notificationManage.createNotificationChannel(notificationChannel!!)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                notificationChannel!!.setSound(defaultSoundUri, audioAttributes)
            }

            builder = Notification.Builder(applicationContext, channelId)
                .setContentTitle(texte)
                .setSmallIcon(R.drawable.main_logo)
                .setLargeIcon(Utility.getBitmapFromURL(urlPicture))
                .setOnlyAlertOnce(true)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()
            notificationManage.notify(0, builder)

        }
        else{
            Timber.e("bad")
            @Suppress("DEPRECATION")
            builder = Notification.Builder(applicationContext)
                .setContentTitle(texte)
                .setSmallIcon(R.drawable.main_logo)
                .setContentText(texte)
                .setContentIntent(pi)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .build()
            notificationManage.notify(0, builder)
        }
    }
}