package com.vguivarc.wakemeup.transport.ringinglist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.Song

@Preview
@Composable
fun RingingCardPreview() {
    RingingCard(
        viewModel = null,
        ringing
        = Ringing(id="id1",song= Song("kgkljhl", "chanson",""), listened = false, seen = false, receiverId = "receiverId", senderId = null, senderName = "senderName")
    )
}
@Composable
fun RingingCard(viewModel: RingingListViewModel?, ringing: Ringing) {
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
            text = "Envoyé par ${ringing.senderName}",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}