package com.vguivarc.wakemeup.transport.contactlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Contact
import timber.log.Timber

@Preview
@Composable
fun ContactCardPreview() {
    ContactCard(
        contact = Contact("id1", "pierre",null,1,2),
        onClick = {
            Timber.d("Click on contact")
        })
}
@Composable
fun ContactCard(contact: Contact, onClick: (contact: Contact) -> Unit) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.empty_picture_profil),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(16.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = contact.username,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = "Nb reçues : ${contact.nbSongReceived} et Nb envoyees : ${contact.nbSongSent}",
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}