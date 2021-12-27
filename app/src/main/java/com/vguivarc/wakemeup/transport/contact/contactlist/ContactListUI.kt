package com.vguivarc.wakemeup.transport.contact.contactlist

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.transport.routeViewModel
import com.vguivarc.wakemeup.util.multifab.MultiFabItem
import com.vguivarc.wakemeup.util.multifab.MultiFabState
import com.vguivarc.wakemeup.util.multifab.MultiFloatingActionButton
import timber.log.Timber

@Composable
fun ContactListScreen(navController: NavController) {
    val contactListViewModel: ContactListViewModel = remember { navController.routeViewModel() }

    val state by contactListViewModel.container.stateFlow.collectAsState()

    val side by contactListViewModel.container.sideEffectFlow.collectAsState(initial = null)

    ContactListContent(
        contacts = state.contactList,
        onAddFacebook = {
            contactListViewModel.addFacebookSelected()
        }
    )
    side?.let {
        handleSideEffect(contactListViewModel, LocalContext.current, navController, it)
    }
}

@Composable
fun ContactListContent(
    contacts: List<Contact>,
    onAddFacebook: () -> Unit
) {
    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    Scaffold(
        floatingActionButton = {
            MultiFloatingActionButton(
                painterResource(id = R.drawable.add_24),
                listOf(
                    MultiFabItem(
                        "add_facebook",
                        painterResource(id = R.drawable.add_24),
                        "Facebook"
                    )/*,
                        MultiFabItem(
                            "add_phone",
                            painterResource(id = R.drawable.add_24), "Phone"
                        )*/
                ), toState, true, { state ->
                    toState = state
                }
            ) {
                when (it.identifier) {
                    "add_facebook" -> {
                        onAddFacebook()
                    }
                }
            }
        },
        content = {
            Column {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactCard(contact)
                    }
                }
            }
        }
    )
}


private fun handleSideEffect(
    contactListViewModel: ContactListViewModel,
    context: Context, navController: NavController, sideEffect: ContactListScreenSideEffect
) {
    when (sideEffect) {
        is ContactListScreenSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
        is ContactListScreenSideEffect.NavigateToContactDetail -> {
            navController.navigate("ContactFacebookList")
            /*navController.navigate(
                ContactListFragmentDirections.displayContactDetail(sideEffect.contact)
            )*/
        }
        ContactListScreenSideEffect.NavigateToAddFacebookContact -> {
            /* navController.navigate(
                 ContactListFragmentDirections.actionAddContactFacebook()
             )*/
        }
    }
}

@Preview
@Composable
fun ContactListContentPreview() {
    ContactListContent(
        contacts = mutableListOf(
            Contact("id1", "pierre", null, 1, 2),
            Contact("id2", "paul", null, 3, 4),
            Contact("id3", "jack", null, 5, 6),
            Contact("id4", "pierre", null, 1, 2),
            Contact("id5", "paul", null, 3, 4),
            Contact("id6", "jack", null, 5, 6),
            Contact("id7", "pierre", null, 1, 2),
            Contact("id8", "paul", null, 3, 4),
            Contact("id9", "jack", null, 5, 6),
            Contact("id10", "pierre", null, 1, 2),
            Contact("id11", "paul", null, 3, 4),
            Contact("id31", "jack", null, 5, 6),
            Contact("id12", "pierre", null, 1, 2),
            Contact("id21", "paul", null, 3, 4),
            Contact("id33", "jack", null, 5, 6),
            Contact("id13", "pierre", null, 1, 2),
            Contact("id24", "paul", null, 3, 4),
            Contact("id34", "jack", null, 5, 6)
        ),
        onAddFacebook = {
            Timber.d("Add Facebook")
        }
    )
}

