package com.vguivarc.wakemeup.notification

import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.entity.UserModel

open class NotificationMusicMe(
    val idReceiver: String,
    val idSender: String?,
    val usernameSender: String?,
    val urlPicture: String?,
    val vue: Boolean,
    val type: NotificationType
) {
    // var sender: UserModel?=null
    var sonnerieName: String? = null

    @Suppress("unused")
    constructor() : this(
        "", null, "", null, false, NotificationType.ENVOIE_MUSIQUE
    )

    enum class NotificationType {
        ENVOIE_MUSIQUE,
        SONNERIE_UTILISEE
    }

    companion object {
        fun newInstanceEnvoieMusique(ringing: Ringing, sender: UserModel): NotificationMusicMe {
            return NotificationMusicMe(ringing.idReceiver, sender.id, sender.username, sender.imageUrl, false, NotificationType.ENVOIE_MUSIQUE)
        }
        fun newInstanceSonnerieUtilisee(ringing: Ringing, sender: UserModel): NotificationMusicMe {
            val not = NotificationMusicMe(
                ringing.senderId,
                sender.id,
                sender.username,
                sender.imageUrl,
                false,
                NotificationType.SONNERIE_UTILISEE
            )
            not.sonnerieName = ringing.song!!.title
            return not
        }
    }
}
