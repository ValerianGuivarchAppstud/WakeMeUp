package com.vguivarc.wakemeup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vguivarc.wakemeup.connect.ui.edituser.EditUser

import com.vguivarc.wakemeup.util.resetFavoris
import com.vguivarc.wakemeup.util.resetHistorique
import com.vguivarc.wakemeup.util.resetHistoriqueVideo

class SettingsUser() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        //Bouton pour gérer l'historique
        view.findViewById<Button>(R.id.button_gestion_historique).setOnClickListener{
            Toast.makeText(
                requireActivity().application,
                "L'historique a été supprimé.",
                Toast.LENGTH_SHORT
            ).show()
            resetHistorique(this.requireContext())
            resetHistoriqueVideo(this.requireContext())
        }

        //Bouton pour editer le compte utilisateur
        view.findViewById<Button>(R.id.button_edit_user).setOnClickListener{
            val intent = Intent(activity, EditUser::class.java)
            startActivity(intent)
        }

        //Bouton pour gérer les favoris
        view.findViewById<Button>(R.id.button_gestionFavori).setOnClickListener{
            Toast.makeText(
                requireActivity().application,
                "Les vidéos favoris ont été surpimées.",
                Toast.LENGTH_SHORT
            ).show()
            resetFavoris(this.requireContext())
        }

        return view
    }





}