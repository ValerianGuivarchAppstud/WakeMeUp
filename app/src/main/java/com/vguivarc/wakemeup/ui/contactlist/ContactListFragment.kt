package com.vguivarc.wakemeup.ui.contactlist

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseLceFragment
import com.vguivarc.wakemeup.base.Fail
import com.vguivarc.wakemeup.base.Loading
import com.vguivarc.wakemeup.base.Success
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.util.widget.FabSmall
import org.koin.android.ext.android.inject

class ContactListFragment :
    BaseLceFragment(R.layout.fragment_contact_list),
    ContactListAdapter.RecyclerItemClickListener {

    private var contactList = mutableListOf<Contact>()
    private lateinit var contactAdapter: ContactListAdapter

    private val viewModelContactList: ContactListViewModel by inject()
    private lateinit var recyclerView: RecyclerView

    private lateinit var fabAddContact : FloatingActionButton
    private lateinit var fabAddContactFb : FabSmall
    private lateinit var fabAddContactExt : FabSmall
    private var isFabOpen = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.contentView)

        contactAdapter = ContactListAdapter(contactList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = contactAdapter

        viewModelContactList.getContactList()
        viewModelContactList.contactList.observe(
            viewLifecycleOwner,
            {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        val contactList = it.data ?: emptyList()
                        refreshUI(contactList)
                        if (contactList.isEmpty()) showEmptyView() else showContent()
                    }
                    is Fail -> showError()
                }
            }
        )

        fabAddContact=view.findViewById(R.id.fab_add_contact)
        fabAddContact.setOnClickListener{
            toggleFabMenu()
        }
        fabAddContactFb=view.findViewById(R.id.add_contact_fb)
        fabAddContactExt=view.findViewById(R.id.add_contact_externe)

        fabAddContactFb.setOnClickListener {
            findNavController().navigate(
                R.id.contactFacebookListFragment
            )
        }

        fabAddContactExt.setOnClickListener {
//            viewModelContact.partagerCodeAjoutExterne(requireContext())
        }
    }

    private fun refreshUI(contactList: List<Contact>) {
        contactAdapter.setContactList(contactList)
    }

    override fun onContactListener(contact: Contact) {
        TODO("Not yet implemented")
    }

    // --------------------------------------------------------------------------------------------------------


    private fun toggleFabMenu() {
        if(!isFabOpen){
            ViewCompat.animate(fabAddContact)
                .rotation(45f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(3f))
                .start()
            openFabMenu(fabAddContactExt)
            openFabMenu(fabAddContactFb)
        } else {
            ViewCompat.animate(fabAddContact)
                .rotation(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(3f))
                .start()
            closeFabMenu(fabAddContactExt)
            closeFabMenu(fabAddContactFb)
        }
        isFabOpen=!isFabOpen
    }

    private fun openFabMenu(fabSmall: FabSmall) {
        ViewCompat.animate(fabSmall)
            .translationY(-fabSmall.offsetYAnimation)
            .withStartAction { fabSmall.visibility = View.VISIBLE }
            .withEndAction { fabSmall.fabText.animate()
                .alpha(1.0f)
                .duration=200}
            .start()
    }


    private fun closeFabMenu(fabSmall: FabSmall) {
        ViewCompat.animate(fabSmall)
            .translationY(0f)
            .withStartAction { fabSmall.fabText.animate().alpha(0f) }
            .withEndAction { fabSmall.visibility=View.GONE }
            .start()
    }
}
