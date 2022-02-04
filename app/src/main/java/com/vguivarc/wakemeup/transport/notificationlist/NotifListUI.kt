package com.vguivarc.wakemeup.transport.notificationlist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.vguivarc.wakemeup.domain.external.entity.Notif
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.transport.routeViewModel

@Composable
fun NotifListScreen(navController: NavController) {
    val notificationListViewModel: NotifListViewModel =
        remember { navController.routeViewModel() }
    val state by notificationListViewModel.container.stateFlow.collectAsState()

    val side by notificationListViewModel.container.sideEffectFlow.collectAsState(initial = null)

    NotifListContent(
        notifications = state.notificationList,
        viewModel = notificationListViewModel
    )
    side?.let {
        handleSideEffect(notificationListViewModel, LocalContext.current, navController, it)
    }
    notificationListViewModel.ok()
}

@Composable
fun NotifListContent(
    notifications: List<Notif>,
    viewModel: NotifListViewModel?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//            Text(text = "lol")
//            NotifCard(viewModel, contacts.first())
        Scaffold(
            content = {
                Column {
                    LazyColumn {
                        items(notifications) { contact ->
                            NotifCard(viewModel, contact)
                        }
                    }
                }
            }
        )
    }
}


private fun handleSideEffect(
    viewModel: NotifListViewModel,
    context: Context, navController: NavController, sideEffect: NotifListSideEffect
) {
    when (sideEffect) {
        is NotifListSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
fun NotifListContentPreview() {
    NotifListContent(
        notifications = mutableListOf(
          //  Notif(id="id1",song= Song("kgkljhl", "chanson",""), listened = false, seen = false, receiverId = "lkgk;", senderId = null, senderName = "joe"),
           // Notif(id="id2",song= Song("sgsgs", "chanson2",""), listened = true, seen = false, receiverId = "lkgk;", senderId = null, senderName = "sfgs")
        ), null
    )
}
