package com.vguivarc.wakemeup.notification

import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.sonnerie.Sonnerie


open class NotificationMusicMe (
    val idReceiver: String,
    val senderId: String,
    val vue : Boolean,
    val type : NotificationType)  {
    var sender: UserModel?=null
    var sonnerieName : String? = null

    constructor() : this(
        "", "", false, NotificationType.AJOUT_CONTACT
    )

     enum class NotificationType{
         AJOUT_CONTACT,
         ENVOIE_MUSIQUE,
         SONNERIE_UTILISEE
     }

     companion object {
         fun newInstance_AjoutContact(idReceiver: String, senderId: String): NotificationMusicMe {
             return NotificationMusicMe(idReceiver, senderId, false, NotificationType.AJOUT_CONTACT)
         }
         fun newInstance_Envoie_Musique(sonnerie: Sonnerie): NotificationMusicMe {
             return NotificationMusicMe(sonnerie.idReceiver, sonnerie.senderId, false, NotificationType.ENVOIE_MUSIQUE)
         }
         fun newInstance_SonnerieUtilisee(sonnerie: Sonnerie): NotificationMusicMe {
             val not= NotificationMusicMe(sonnerie.idReceiver, sonnerie.senderId, false, NotificationType.SONNERIE_UTILISEE)
             not.sonnerieName=sonnerie.song!!.title
             return not
         }
     }
}