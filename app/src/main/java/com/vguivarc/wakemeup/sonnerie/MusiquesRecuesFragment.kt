package com.vguivarc.wakemeup.sonnerie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.song.Song
import kotlinx.android.synthetic.main.fragment_musiques_recues.view.*

class MusiquesRecuesFragment : Fragment() {

    private lateinit var currentView: View

    private var fragmentMusiquesPassees: MusiquesPasseesFragment? = null
    private var fragmentMusiquesAttente: MusiquesAttenteFragment? = null



    //Gestion du clic sur le bouton En attente ou Passees
    private fun gestionSwitchAttentePassees(){
        val btEnAttente = currentView.bouton_musiques_en_attente
        val btPassees = currentView.bouton_musiques_passees

        btEnAttente.setOnClickListener {
            if (!btEnAttente.isActivated){

                btEnAttente.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
                btEnAttente.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                btPassees.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                btPassees.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))

                btPassees.isActivated = false
                btEnAttente.isActivated = true

                showFragment(FragmentId.FRAGMENT_ATTENTE)
            }
        }

        btPassees.setOnClickListener {
            if (!btPassees.isActivated){
                btPassees.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
                btPassees.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                btEnAttente.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                btEnAttente.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))

                btPassees.isActivated = true
                btEnAttente.isActivated = false

                showFragment(FragmentId.FRAGMENT_PASSEES)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentView = inflater.inflate(R.layout.fragment_musiques_recues, container, false)


        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gestionSwitchAttentePassees()
        showFirstFragment()
    }


    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private fun showFirstFragment() {
        val visibleFragment =
            childFragmentManager.findFragmentById(R.id.child_fragment_container)
        if (visibleFragment == null) {
            // Show News Fragment
            this.showFragment(FragmentId.FRAGMENT_ATTENTE)
        }
    }

    // Show fragment according an Identifier

    private fun showFragment(fragmentIdentifier: FragmentId) {
        when (fragmentIdentifier) {
            FragmentId.FRAGMENT_PASSEES -> this.showPasseesFragment()
            FragmentId.FRAGMENT_ATTENTE -> this.showAttenteFragment()
        }
    }



    // ---

    // Create each fragment page and show it

    private fun showAttenteFragment() {
        if (this.fragmentMusiquesAttente == null) {
            this.fragmentMusiquesAttente = MusiquesAttenteFragment.newInstance()
        }
        childFragmentManager.beginTransaction().remove(this.fragmentMusiquesAttente!!).commit()
        this.startTransactionFragment(this.fragmentMusiquesAttente!!)
    }

    private fun showPasseesFragment() {
        if (this.fragmentMusiquesPassees == null) {
            this.fragmentMusiquesPassees = MusiquesPasseesFragment.newInstance(this)
        }
        childFragmentManager.beginTransaction().remove(this.fragmentMusiquesPassees!!).commit()
        this.startTransactionFragment(this.fragmentMusiquesPassees!!)
    }

    // ---

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private fun startTransactionFragment(fragment: Fragment) {
        if (!fragment.isVisible) {
            childFragmentManager.beginTransaction()
                .replace(R.id.child_fragment_container, fragment).commit()
        }
    }

    fun share(song: Song) {
        val action = MusiquesRecuesFragmentDirections.actionMusiquesRecuesFragmentToContactsListeShareFragment(song)
        findNavController().navigate(action)

//        val action = MusiquesPasseesFragmentDirections.actionMusiquesPasseesFragmentToContactsListeShareFragment(sonnerie.song!!)
  //      findNavController().navigate(action)

    }

    fun name(user: FirebaseUser?) {
        if(user!=null){
            val action = MusiquesRecuesFragmentDirections.actionMusiquesRecuesFragmentToContactFragment(user)
            findNavController().navigate(action)
        }
    }


    companion object {

        fun newInstance(): MusiquesRecuesFragment {
            return MusiquesRecuesFragment()
        }

        enum class FragmentId {
            FRAGMENT_PASSEES,
            FRAGMENT_ATTENTE
        }
    }
}


