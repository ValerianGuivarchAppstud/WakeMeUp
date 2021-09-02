package com.vguivarc.wakemeup.ui.contactlistfacebook

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.ContactFacebook
import kotlinx.android.synthetic.main.fragment_auth_email.*
import org.koin.android.ext.android.inject

class ContactFacebookFragment :
    BaseLceFragment(R.layout.fragment_contact_facebook_list),
    ContactFacebookAdapter.ContactFacebookItemClickListener {

    private val contactFacebookList = mutableListOf<ContactFacebook>()
    private lateinit var contactFacebookAdapterSearch: ContactFacebookAdapter
    private val contactFacebookViewModel: ContactFacebookViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).setSupportActionBar(loginEmailToolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        contactFacebookAdapterSearch = ContactFacebookAdapter(requireContext(), contactFacebookList, this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.contentView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = contactFacebookAdapterSearch
        contactFacebookAdapterSearch.notifyDataSetChanged()

        contactFacebookViewModel.getContactFacebookList()
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
