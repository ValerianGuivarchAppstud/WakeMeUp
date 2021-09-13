package com.vguivarc.wakemeup.transport.contactlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.transport.ui.theme.WakeMeUpTheme
import com.vguivarc.wakemeup.util.multifab.MultiFabItem
import com.vguivarc.wakemeup.util.multifab.MultiFabState
import com.vguivarc.wakemeup.util.multifab.MultiFloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class ContactListFragment : Fragment() {

    private val viewModel by viewModel<ContactListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WakeMeUpTheme {
                    ContactListScreen(findNavController(), viewModel)
                }
            }
        }
    }



    private fun handleSideEffect(navController: NavController, sideEffect: ContactListScreenSideEffect) {
        when (sideEffect) {
            is ContactListScreenSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
            is ContactListScreenSideEffect.NavigateToContactDetail -> {
                /*navController.navigate(
                    ContactListFragmentDirections.displayContactDetail(sideEffect.contact)
                )*/
            }
            ContactListScreenSideEffect.NavigateToAddFacebookContact -> {
                navController.navigate(
                    ContactListFragmentDirections.actionAddContactFacebook()
                )
            }
        }
    }

    @Composable
    fun ContactListScreen(navController: NavController, contactListViewModel: ContactListViewModel) {
        val state by contactListViewModel.container.stateFlow.collectAsState()

        val side by contactListViewModel.container.sideEffectFlow.collectAsState(initial = null)

        ContactListContent(
            contacts = state.contactList,
            onContactSelected = { contact: Contact ->
                contactListViewModel.onContactSelected(contact)
            }
        )
        side?.let {
            handleSideEffect(navController, it)
        }
    }

    @Composable
    fun ContactListContent(
        contacts: List<Contact>,
        onContactSelected: (contact: Contact) -> Unit
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
                        ),
                        MultiFabItem(
                            "add_phone",
                            painterResource(id = R.drawable.add_24), "Phone"
                        )
                    ), toState, true, { state ->
                        toState = state
                    }
                ) {
                    when(it.identifier){
                        "add_facebook" -> {
                            viewModel.addFacebookSelected()
                        }
                    }
                }
            },
            content = {
                Column {
                    LazyColumn {
                        items(contacts) { contact ->
                            ContactCard(contact, onContactSelected)
                        }
                    }
                }
            }
        )
    }

    @Preview
    @Composable
    fun ContactListContentPreview() {
        ContactListContent(contacts = mutableListOf(
            Contact("id1", "pierre",null,1,2),
            Contact("id2", "paul",null,3,4),
            Contact("id3", "jack",null,5,6),
            Contact("id4", "pierre",null,1,2),
            Contact("id5", "paul",null,3,4),
            Contact("id6", "jack",null,5,6),
            Contact("id7", "pierre",null,1,2),
            Contact("id8", "paul",null,3,4),
            Contact("id9", "jack",null,5,6),
            Contact("id10", "pierre",null,1,2),
            Contact("id11", "paul",null,3,4),
            Contact("id31", "jack",null,5,6),
            Contact("id12", "pierre",null,1,2),
            Contact("id21", "paul",null,3,4),
            Contact("id33", "jack",null,5,6),
            Contact("id13", "pierre",null,1,2),
            Contact("id24", "paul",null,3,4),
            Contact("id34", "jack",null,5,6)
            ), onContactSelected = {
            Timber.d("Je clique sur ${it.username}")
        })
    }


}

