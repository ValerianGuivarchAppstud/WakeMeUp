package com.vguivarc.wakemeup.transport.contactlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentContactListBinding
import com.vguivarc.wakemeup.domain.external.entity.Contact
import com.vguivarc.wakemeup.util.widget.FabSmall
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber


class ContactListFragment : Fragment(R.layout.fragment_contact_list),
    ContactListAdapter.RecyclerItemClickListener {

    private val viewModel by viewModel<ContactListViewModel>()

    private var _binding: FragmentContactListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactListSwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        binding.buttonAddContact.setOnClickListener {
            Timber.e("lol")
            viewModel.addContactButton()
        }
    }

    private fun refreshData() {
        viewModel.getContactList()
        binding.contactListSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: ContactListState) {
        Timber.d("render $state")
        binding.loader.isVisible = state.isLoading
        binding.contactListSwipeRefreshLayout.isVisible = !state.isLoading
        toggleFabMenu(state.isFabOpen)
        val list = view?.findViewById<RecyclerView>(R.id.contactList) ?: return
        if (list.adapter == null) {
            list.adapter = ContactListAdapter(state.contactList, this)
        } else {
            (list.adapter as? ContactListAdapter)?.updateData(state.contactList)
        }
    }

    private fun handleSideEffect(sideEffect: ContactListSideEffect) {
        when (sideEffect) {
            is ContactListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onContactListener(contact: Contact) {
        TODO("Not yet implemented")
    }


    private fun toggleFabMenu(isFabOpen: Boolean) {
        if(isFabOpen){
            ViewCompat.animate(binding.fabAddContact)
                .rotation(45f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(3f))
                .start()
            openFabMenu(binding.fabAddContactExt)
            openFabMenu(binding.fabAddContactFb)
        } else {
            ViewCompat.animate(binding.fabAddContact)
                .rotation(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(3f))
                .start()
            closeFabMenu(binding.fabAddContactExt)
            closeFabMenu(binding.fabAddContactFb)
        }
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

