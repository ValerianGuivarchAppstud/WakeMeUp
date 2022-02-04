package com.vguivarc.wakemeup.transport.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.SearchSong
import com.vguivarc.wakemeup.domain.external.entity.Song

@Preview
@Composable
fun SearchSongCardPreview() {
    SearchSongCard(
        searchSongListViewModel = null,
        searchSong = SearchSong(Song("kfgkl", "example song"), false)
    )
}
@Composable
fun SearchSongCard(searchSongListViewModel: SearchSongListViewModel?, searchSong: SearchSong) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
        ) {
            val (video,play, title, share, favorite) = createRefs()

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
                    .size(30.dp)
                    .constrainAs(play) {
                        top.linkTo(video.top, margin = 0.dp)
                        bottom.linkTo(video.bottom, margin = 0.dp)
                        start.linkTo(video.start, margin = 0.dp)
                        end.linkTo(video.end, margin = 0.dp)
                    }
                        )
            Text(
                modifier = Modifier.constrainAs(title){
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(video.end, margin = 8.dp)
                    end.linkTo(favorite.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
//                    end.linkTo(share.start, margin = 8.dp)
                },
                text = searchSong.song.title,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

        Box(modifier = Modifier.constrainAs(share){
            top.linkTo(parent.top, margin = 8.dp)
            end.linkTo(parent.end, margin = 8.dp)
        }.clickable { searchSongListViewModel?.updateFavorite(searchSong.song, searchSong.isFavorite.not()) }) {
                Icon(Icons.Filled.Share, "Share song")
        }
            Box(modifier = Modifier.constrainAs(favorite){
                top.linkTo(share.bottom, margin = 8.dp)
                end.linkTo(parent.end, margin = 8.dp)
            }.clickable { searchSongListViewModel?.updateFavorite(searchSong.song, searchSong.isFavorite.not()) }) {
                if (searchSong.isFavorite) {
                    Icon(Icons.Filled.Favorite, "Favorite song")
                } else {
                    Icon(Icons.Filled.FavoriteBorder, "Favorite song")
                }
            }
        }


}