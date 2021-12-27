package com.vguivarc.wakemeup.transport.contact.contactlistfacebook

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
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.transport.routeViewModel


@Composable
fun ContactFacebookListScreen(navController: NavController) {
    val contactListFacebookViewModel: ContactFacebookListViewModel =
        remember { navController.routeViewModel() }
    val state by contactListFacebookViewModel.container.stateFlow.collectAsState()

    val side by contactListFacebookViewModel.container.sideEffectFlow.collectAsState(initial = null)

    ContactFacebookListContent(
        contacts = state.contactFacebookList,
        viewModel = contactListFacebookViewModel
    )
    side?.let {
        handleSideEffect(contactListFacebookViewModel, LocalContext.current, navController, it)
    }
    contactListFacebookViewModel.ok()
}

@Composable
fun ContactFacebookListContent(
    contacts: List<ContactFacebook>,
    viewModel: ContactFacebookListViewModel?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//            Text(text = "lol")
//            ContactFacebookCard(viewModel, contacts.first())
        Scaffold(
            content = {
                Column {
                    LazyColumn {
                        items(contacts) { contact ->
                            ContactFacebookCard(viewModel, contact)
                        }
                    }
                }
            }
        )
    }
}


private fun handleSideEffect(
    viewModel: ContactFacebookListViewModel,
    context: Context, navController: NavController, sideEffect: ContactFacebookListSideEffect
) {
    when (sideEffect) {
        is ContactFacebookListSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
fun ContactFacebookListContentPreview() {
    ContactFacebookListContent(
        contacts = mutableListOf(
            ContactFacebook("id1", "idf1", "pierre", null, false),
            ContactFacebook("id2", "idf2", "paul", null, true),
            ContactFacebook("id3", "idf3", "jack", null, false),
            ContactFacebook("id4", "idf4", "pierre", null, true),
        ), viewModel = null
    )
}


/*
class ContactFacebookListFragment :
    BaseLceFragment(R.layout.fragment_contact_facebook_list),
    ContactFacebookListAdapter.ContactFacebookItemClickListener {

    private val contactFacebookList = mutableListOf<ContactFacebook>()
    private lateinit var contactFacebookAdapterSearch: ContactFacebookListAdapter
    private val contactFacebookViewModel: ContactFacebookListViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).setSupportActionBar(loginEmailToolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contactFacebookAdapterSearch = ContactFacebookListAdapter(requireContext(), contactFacebookList, this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contentView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = contactFacebookAdapterSearch
        contactFacebookAdapterSearch.notifyDataSetChanged()

        val accessToken = AccessToken.getCurrentAccessToken()
        contactFacebookViewModel.getContactFacebookList(accessToken.token)
        contactFacebookViewModel.contactFacebookList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        refreshUI(it.data ?: listOf())
                        if (it.data.isNullOrEmpty())
                            showEmptyView()
                        else
                            showContent()
                    }
                    is Fail -> showError()
                }
            }
        )

        // Si on est a la fin du recycler view---------------------------------------------------
        /*  recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
              override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                  super.onScrollStateChanged(recyclerView, newState)
                  if (!recyclerView.canScrollVertically(1)) {
                      viewModelSearchVideo.addSearchVideo()
                  }
              }
          })*/
        // ---------------------------------------------------------------------------------------



    }

    // --------------------------------------------------

    // Gestion du clic sur le bouton partage
            /* private fun gestionBoutonPartage(){
                btPartage.setOnClickListener {
                     if (AndroidApplication.repository.getCurrentUser()==null) {
                     //TODO remplacer par alertdialog (en commentaire) pour envoyer vers le fragment de connection
                         Utility.createSimpleToast(AndroidApplication.appContext.resources.getString(R.string.vous_netes_pas_connecte))
                     } else {
                         if (currentSong != null) {
         /*                    val action =
                                 com.vguivarc.wakemeup.song.search.RechercheVideoFragmentDirections.actionRechercheVideoFragmentToContactsListeShareFragment(
                                     currentSong!!
                                 )
                             findNavController().navigate(action)*/
                         }
                         else{
                             Toast.makeText(
                                 requireActivity().application,
                                 "Veuillez sélectionner une vidéo",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }
                 }
             }*/


    private fun refreshUI(listContactFacebook: List<ContactFacebook>) {
        contactFacebookAdapterSearch.setListContactFacebook(listContactFacebook)
    }
}
*/