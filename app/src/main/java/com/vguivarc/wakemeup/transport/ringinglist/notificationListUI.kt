package com.vguivarc.wakemeup.transport.ringinglist

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
import com.vguivarc.wakemeup.domain.external.entity.Ringing
import com.vguivarc.wakemeup.domain.external.entity.Song
import com.vguivarc.wakemeup.transport.routeViewModel

@Composable
fun RingingListScreen(navController: NavController) {
    val ringingListViewModel: RingingListViewModel =
        remember { navController.routeViewModel() }
    val state by ringingListViewModel.container.stateFlow.collectAsState()

    val side by ringingListViewModel.container.sideEffectFlow.collectAsState(initial = null)

    RingingListContent(
        ringings = state.ringingList,
        viewModel = ringingListViewModel
    )
    side?.let {
        handleSideEffect(ringingListViewModel, LocalContext.current, navController, it)
    }
    ringingListViewModel.ok()
}

@Composable
fun RingingListContent(
    ringings: List<Ringing>,
    viewModel: RingingListViewModel?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//            Text(text = "lol")
//            RingingCard(viewModel, contacts.first())
        Scaffold(
            content = {
                Column {
                    LazyColumn {
                        items(ringings) { contact ->
                            RingingCard(viewModel, contact)
                        }
                    }
                }
            }
        )
    }
}


private fun handleSideEffect(
    viewModel: RingingListViewModel,
    context: Context, navController: NavController, sideEffect: RingingListSideEffect
) {
    when (sideEffect) {
        is RingingListSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
fun RingingListContentPreview() {
    RingingListContent(
        ringings = mutableListOf(
            Ringing(id="id1",song= Song("kgkljhl", "chanson",""), listened = false, seen = false, receiverId = "lkgk;", senderId = null, senderName = "joe"),
            Ringing(id="id2",song= Song("sgsgs", "chanson2",""), listened = true, seen = false, receiverId = "lkgk;", senderId = null, senderName = "sfgs")
        ), null
    )
}
