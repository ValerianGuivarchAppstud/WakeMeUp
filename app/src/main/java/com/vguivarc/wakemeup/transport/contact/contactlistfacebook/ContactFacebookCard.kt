package com.vguivarc.wakemeup.transport.contact.contactlistfacebook

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook

@Preview
@Composable
fun ContactFacebookCardPreview() {
    ContactFacebookCard(
        viewModel = null,
        contact = ContactFacebook("id1", "kgkljhl", "pierre",null,true)
    )
}
@Composable
fun ContactFacebookCard(viewModel: ContactFacebookListViewModel?, contact: ContactFacebook) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.empty_picture_profil),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(0.dp)
                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = contact.username,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { viewModel?.actionAddFacebookContact(contact) }) {
            if(contact.contact) {
                Icon(Icons.Filled.PersonRemove, "Remove facebook contact")
            } else {
                Icon(Icons.Filled.PersonAdd, "Add facebook contact")
            }
        }
    }
}