package com.vguivarc.wakemeup.contact

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.CurrentUserViewModel
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility


class ContactFragment : Fragment() {

    private lateinit var viewModelContact : ContactListeViewModel
    private lateinit var favoriButton : Button
    private lateinit var viewModelSonnerie : SonnerieListeViewModel

    private var  currentUser : UserModel? = null
    private lateinit var currentUserViewModel: CurrentUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contact = ContactFragmentArgs.fromBundle(requireArguments()).contact
        val view =  inflater.inflate(R.layout.fragment_contact, container, false)

        view.findViewById<TextView>(R.id.id_contact_nom).setText(contact.username)
        if(contact.imageUrl!="") {
            Glide.with(requireContext())
                .load(contact.imageUrl)
                .into(view.findViewById<ImageView>(R.id.profil_picture))
        } else {
            view.findViewById<ImageView>(R.id.profil_picture).setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.empty_picture_profil))
        }

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelContact = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)
        favoriButton = view.findViewById<Button>(R.id.id_contact_partage_favori)

        currentUserViewModel.getCurrentUserLiveData().observe(requireActivity(), androidx.lifecycle.Observer {
            currentUser=it
            if (currentUser==null) {
                favoriButton.visibility=View.GONE
            }
        })

        view.findViewById<Button>(R.id.id_contact_partage_favori).setOnClickListener{
            val action = ContactFragmentDirections.actionContactFragmentToFavorisShareFragment(contact)
            findNavController().navigate(action)
        }


        view.findViewById<Button>(R.id.id_contact_saisie_musique).setOnClickListener{
            val layout: View = inflater.inflate(
                R.layout.dialog_saisie_youtube_manuelle,
                container, false
            )
            if (currentUser==null) {
                layout.findViewById<TextView>(R.id.id_dialog_saisie_manuel_text2).visibility=View.VISIBLE
                layout.findViewById<EditText>(R.id.id_dialog_saisie_manuel_pseudo).visibility=View.VISIBLE
            }
            val urlYT =
                layout.findViewById<View>(R.id.id_dialog_saisie_manuel_url) as EditText
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(layout)
            builder.setPositiveButton(
                "Envoyer"
            ) { dialog, which ->
                dialog.dismiss()
                if (currentUser==null) {
                    viewModelSonnerie.addSonnerieUrlToUser(requireActivity(), urlYT.text.toString(), contact,
                        layout.findViewById<EditText>(R.id.id_dialog_saisie_manuel_pseudo).text.toString())
                } else {
                    viewModelSonnerie.addSonnerieUrlToUser(requireActivity(), urlYT.text.toString(), contact, null)
                }
            }
            builder.setNegativeButton(
                "Annuler"
            ) { dialog, which -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }

        viewModelSonnerie.getSonnerieStateAddResult().observe(requireActivity(), Observer {
            if(it.error==null){
                Utility.createSimpleToast("Sonnerie envoyée")
            } else {
                Utility.createSimpleToast("Erreur dans l'envoie de la sonnerie")
                Log.e("ContactFragment", it.error.toString())
            }
        })


        return view

    }
}
