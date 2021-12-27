package com.vguivarc.wakemeup.transport.favoritelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.domain.external.entity.Song

@Preview
@Composable
fun FavoriteCardPreview() {
    FavoriteCard(
        favoriteListViewModel = null,
        favorite = Favorite("createdAt", Song("idSong", "idName", "url"))
    )
}
@Composable
fun FavoriteCard(favoriteListViewModel: FavoriteListViewModel?, favorite: Favorite) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { favoriteListViewModel?.selectFavorite(favorite) }
        .padding(all = 8.dp)) {
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
                text = favorite.song.title,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}