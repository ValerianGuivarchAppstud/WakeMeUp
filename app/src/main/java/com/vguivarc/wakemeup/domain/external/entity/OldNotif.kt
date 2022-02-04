package com.vguivarc.wakemeup.domain.external.entity
/*
open class OldNotif(
    val idReceiver: String?,
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
        fun newInstanceEnvoieMusique(ringing: Ringing, sender: UserProfile): OldNotif {
            return OldNotif(ringing.receiverId, sender.profileId, sender.username, sender.imageUrl, false, NotificationType.ENVOIE_MUSIQUE)
        }
        fun newInstanceSonnerieUtilisee(ringing: Ringing, sender: UserProfile): OldNotif {
            val not = OldNotif(
                ringing.senderId,
                sender.profileId,
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
*/