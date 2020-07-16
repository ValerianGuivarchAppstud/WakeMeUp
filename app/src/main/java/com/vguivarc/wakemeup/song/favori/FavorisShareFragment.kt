package com.vguivarc.wakemeup.song.favori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.sonnerie.SonnerieListeViewModel
import com.vguivarc.wakemeup.util.Utility


class FavorisShareFragment : Fragment(), FavorisShareAdaptater.RecyclerItemClickListener {

    private var favorisList: MutableMap<String, Favori> = mutableMapOf()
    private lateinit var fAdapter: FavorisShareAdaptater

    //private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View
    private var selections = mutableListOf<Favori>()

    private lateinit var viewModelSonnerie: SonnerieListeViewModel


    private lateinit var textePasDeFavori: TextView
    private lateinit var loading: ProgressBar

    private lateinit var viewModelFavori: FavorisViewModel
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelFavori = ViewModelProvider(this, factory).get(FavorisViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)

        viewModelFavori.getFavoris()

        //Initialisation des vues-------------------------------------------------------------
        currentView = inflater.inflate(R.layout.fragment_favori_share, container, false)
        fAdapter = FavorisShareAdaptater(favorisList, selections, this)

        textePasDeFavori = currentView.findViewById(R.id.textPasFavori)
        loading = currentView.findViewById(R.id.pb_main_loader)

        viewModelFavori.getFavoriVideosLiveData()
            .observe(requireActivity(), androidx.lifecycle.Observer {
                if (it.error == null) {
                    if (it.favoriList.size == 0) {
                        favorisList.clear()
                        fAdapter.notifyDataSetChanged()
                        textePasDeFavori.visibility = View.VISIBLE
                    } else {
                        favorisList.clear()
                        favorisList.putAll(it.favoriList)
                        fAdapter.notifyDataSetChanged()
                        textePasDeFavori.visibility = View.GONE
                    }
                }
                loading.visibility = View.GONE
            })

        recyclerView = currentView.findViewById(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = fAdapter

        val contact = FavorisShareFragmentArgs.fromBundle(requireArguments()).contact

        currentView.findViewById<Button>(R.id.valid_share_favori_button).setOnClickListener {
            for (f in selections) {
                viewModelSonnerie.addSonnerieToUser(f.song!!, contact)
            }
            if (selections.size > 0) {
                Utility.createSimpleToast("Sonneries envoyées !")
                requireActivity().onBackPressed()
            } else {
                Utility.createSimpleToast("Veuillez sélectionner les musiques à envoyer à votre contact")
            }
        }

        currentView.findViewById<TextView>(R.id.share_contact_name).setText(contact.displayName)
        if (contact.photoUrl.toString() != "") {
            Glide.with(requireContext())
                .load(contact.photoUrl)
                .into(currentView.findViewById(R.id.share_contact_image))
        } else {
            currentView.findViewById<ImageView>(R.id.share_contact_image).setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.empty_picture_profil)
            )
        }

        return currentView
    }

    override fun onSelect(fav: Favori, position: Int) {
        if (selections.contains(fav)) {
            selections.remove(fav)
        } else {
            selections.add(fav)
        }
        fAdapter.notifyDataSetChanged()
    }


}


