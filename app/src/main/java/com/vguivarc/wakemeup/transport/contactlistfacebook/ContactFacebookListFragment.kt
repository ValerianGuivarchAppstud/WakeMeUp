package com.vguivarc.wakemeup.transport.contactlistfacebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.AccessToken
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.databinding.FragmentContactFacebookListBinding
import com.vguivarc.wakemeup.databinding.FragmentContactListBinding
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.domain.external.entity.ContactFacebook
import com.vguivarc.wakemeup.transport.contactlist.ContactListAdapter
import com.vguivarc.wakemeup.transport.contactlist.ContactListSideEffect
import com.vguivarc.wakemeup.transport.contactlist.ContactListState
import com.vguivarc.wakemeup.transport.contactlist.ContactListViewModel
import kotlinx.android.synthetic.main.fragment_auth_email.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber
class ContactFacebookListFragment : Fragment(R.layout.fragment_contact_facebook_list),
    ContactFacebookListAdapter.ContactFacebookItemClickListener {

    private val viewModel by viewModel<ContactFacebookListViewModel>()

    private var _binding: FragmentContactFacebookListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactFacebookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactFacebookListSwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.getContactFacebookList()
        binding.contactFacebookListSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: ContactFacebookListState) {
        Timber.d("render $state")
        binding.loader.isVisible = state.isLoading
        binding.contactFacebookListSwipeRefreshLayout.isVisible = !state.isLoading
        val list = view?.findViewById<RecyclerView>(R.id.contactList) ?: return
        if (list.adapter == null) {
            list.adapter = ContactFacebookListAdapter(state.contactFacebookList, this)
        } else {
            (list.adapter as? ContactFacebookListAdapter)?.updateData(state.contactFacebookList)
        }
    }

    private fun handleSideEffect(sideEffect: ContactFacebookListSideEffect) {
        when (sideEffect) {
            is ContactFacebookListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
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