package com.vguivarc.wakemeup.notification

import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.sonnerie.Sonnerie


open class NotificationMusicMe(
    val idReceiver: String,
    val idSender: String?,
    val usernameSender: String?,
    val urlPicture: String?,
    val vue: Boolean,
    val type: NotificationType
)  {
    //var sender: UserModel?=null
    var sonnerieName : String? = null

    constructor() : this(
        "", null,"", null, false, NotificationType.ENVOIE_MUSIQUE
    )

     enum class NotificationType{
         ENVOIE_MUSIQUE,
         SONNERIE_UTILISEE
     }

     companion object {
         //TODO mettre receveur ici
         fun newInstanceEnvoieMusique(sonnerie: Sonnerie, sender: UserModel): NotificationMusicMe {
             return NotificationMusicMe(sonnerie.idReceiver, sender.id, sender.username, sender.imageUrl, false, NotificationType.ENVOIE_MUSIQUE)
         }
         fun newInstanceSonnerieUtilisee(sonnerie: Sonnerie, sender: UserModel): NotificationMusicMe {
             val not= NotificationMusicMe(
                 sonnerie.idReceiver,
                 sender.id,
                 sender.username,
                 sender.imageUrl,
                 false,
                 NotificationType.SONNERIE_UTILISEE
             )
             not.sonnerieName=sonnerie.song!!.title
             return not
         }
     }
}