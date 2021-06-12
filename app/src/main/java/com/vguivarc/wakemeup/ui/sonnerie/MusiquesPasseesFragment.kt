package com.vguivarc.wakemeup.ui.sonnerie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.AndroidApplication
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.entity.UserModel
import com.vguivarc.wakemeup.repo.ViewModelFactory
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.ui.favori.FavorisViewModel
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
    private val listMusicPass = mutableListOf<Ringing>()
    private val listMusicFav = mutableListOf<Favorite>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       val factory = ViewModelFactory(AndroidApplication.repository)
        viewModelSonneries = ViewModelProvider(requireActivity(), factory).get(
            SonnerieListeViewModel::class.java)
        viewModelSonneries.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            { nouvelleListe ->
                updateMusiqueListe(nouvelleListe)
            })
        viewModelFavoris = ViewModelProvider(requireActivity(), factory).get(FavorisViewModel::class.java)
        viewModelFavoris.getFavoriVideosLiveData().observe(
            viewLifecycleOwner,
            { nouvelleListe ->
                updateFavorisListe(nouvelleListe.favoriList)
            })

    }


    //TODO vérifier si ça marche, avec liste en .values
    //TOD pas le cas !
    private fun updateMusiqueListe(state: Map<String, Ringing>){
        listMusicPass.clear()
        listMusicPass.addAll(state.values)
        mAdapter.notifyDataSetChanged()
        if(listMusicPass.size!=0){
            currentView.findViewById<TextView>(R.id.texte_pas_de_musiques_passees).visibility=View.GONE
        } else {
            currentView.findViewById<TextView>(R.id.texte_pas_de_musiques_passees).visibility=View.VISIBLE
        }

    }

    private fun updateFavorisListe(state: Map<String, Favorite>){
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

    override fun onPlayListener(ringing: Ringing, position: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v="+ringing.song!!.id)
            )
        )
    }


    override fun onDeleteListener(ringing: Ringing, position: Int) {
        viewModelSonneries.deleteSonneriePassee(ringing)
    }

    override fun onFavoriListener(ringing: Ringing, position: Int) {
        if(listMusicFav.filter { fav -> fav.song!!.id == ringing.song!!.id }.isEmpty())
            viewModelFavoris.addFavori(ringing.song!!)
        else
            viewModelFavoris.deleteFavori(ringing.song!!)
    }

    override fun onShareListener(ringing: Ringing, position: Int) {
        fragFather.share(ringing.song!!)
    }

    override fun onNameListener(user: UserModel?, position: Int) {
        fragFather.name(user)
    }

}