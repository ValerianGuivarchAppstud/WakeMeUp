package com.vguivarc.wakemeup.sonnerie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.song.favori.Favori
import com.vguivarc.wakemeup.song.favori.FavorisViewModel
import kotlinx.android.synthetic.main.fragment_musiques_passees.view.*

class MusiquesPasseesFragment : Fragment(), SonneriePasseAdapter.RecyclerItemClickListener {

    private lateinit var fragFather: MusiquesRecuesFragment
    private lateinit var mAdapter: SonneriePasseAdapter
//    private lateinit var mAdapter: SongHistoriqueAdaptater
    //private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View

    private var currentIndex: Int = 0

  //  private var youTubePlayer: YouTubePlayer? = null


    private lateinit var viewModelSonneries: SonnerieListeViewModel
    private lateinit var viewModelFavoris: FavorisViewModel
    private val listMusicPass = mutableListOf<Sonnerie>()
    private val listMusicFav = mutableListOf<Favori>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelSonneries = ViewModelProvider(requireActivity(), factory).get(SonnerieListeViewModel::class.java)
        viewModelSonneries.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            Observer { nouvelleListe ->
                updateMusiqueListe(nouvelleListe)
            })
        viewModelFavoris = ViewModelProvider(requireActivity(), factory).get(FavorisViewModel::class.java)
        viewModelFavoris.getFavoriVideosLiveData().observe(
            viewLifecycleOwner,
            Observer { nouvelleListe ->
                updateFavorisListe(nouvelleListe.favoriList)
            })

    }


    //TODO vérifier si ça marche, avec liste en .values
    //TOD pas le cas !
    private fun updateMusiqueListe(state: Map<String, Sonnerie>){
        listMusicPass.clear()
        listMusicPass.addAll(state.values)
        mAdapter.notifyDataSetChanged()
        if(listMusicPass.size!=0){
            currentView.findViewById<TextView>(R.id.texte_pas_de_musiques_passees).visibility=View.GONE
        } else {
            currentView.findViewById<TextView>(R.id.texte_pas_de_musiques_passees).visibility=View.VISIBLE
        }

    }

    private fun updateFavorisListe(state: Map<String, Favori>){
        listMusicFav.clear()
        listMusicFav.addAll(state.values)
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_musiques_passees, container, false)

        mAdapter = SonneriePasseAdapter(this.requireContext(), listMusicPass, listMusicFav,this)

        val recyclerView = currentView.findViewById<RecyclerView>(R.id.recycler_list_video_musiques_passees)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        //---------------------------------------------------------------------------------------------------

        currentIndex = 0
        currentView.pb_main_loader.visibility = View.GONE
        mAdapter.notifyDataSetChanged()

        return currentView
    }

    companion object {

        fun newInstance(fragFather: MusiquesRecuesFragment): MusiquesPasseesFragment {

            val nf = MusiquesPasseesFragment()
            nf.fragFather=fragFather
            //TODO corriger ça ?
            /*
            val songList: MutableList<Song> = mutableListOf()
            for(hs in nf.songList.list){
                songList.add(hs.song)
            }*/
            return nf
        }
    }

    override fun onPlayListener(sonnerie: Sonnerie, position: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v="+sonnerie.idSong)
            )
        )
    }


    override fun onDeleteListener(sonnerie: Sonnerie, position: Int) {
        viewModelSonneries.deleteSonneriePassee(sonnerie)
    }

    override fun onFavoriListener(sonnerie: Sonnerie, position: Int) {
        if(listMusicFav.filter { fav -> fav.idSong == sonnerie.idSong }.size==0)
            viewModelFavoris.addFavori(sonnerie.song!!)
        else
            viewModelFavoris.deleteFavori(sonnerie.song!!)
    }

    override fun onShareListener(sonnerie: Sonnerie, position: Int) {
        fragFather.share(sonnerie.song!!)
    }

    override fun onNameListener(user: FirebaseUser?, position: Int) {
        fragFather.name(user)
    }

}