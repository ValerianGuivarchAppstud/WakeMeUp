package com.vguivarc.wakemeup.transport.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.domain.external.entity.Song

@Preview
@Composable
fun SearchSongCardPreview() {
    SearchSongCard(
        searchSong = SearchSong(Song("kfgkl", "example song"), false)
    )
}
@Composable
fun SearchSongCard(searchSong: SearchSong) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(all = 8.dp)) {
        ConstraintLayout(
        ) {
            val (video,play) = createRefs()

            Image(
                painter = painterResource(R.drawable.music_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp, 80.dp)
                .constrainAs(video) {
                top.linkTo(parent.top, margin = 0.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
            }
            )
            Image(
                painter = painterResource(R.drawable.ic_play_circle_outline_white),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp).constrainAs(play) {
                        top.linkTo(video.top, margin = 0.dp)
                        bottom.linkTo(video.bottom, margin = 0.dp)
                        start.linkTo(video.start, margin = 0.dp)
                        end.linkTo(video.end, margin = 0.dp)
                    }
                        )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = searchSong.song.title,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}