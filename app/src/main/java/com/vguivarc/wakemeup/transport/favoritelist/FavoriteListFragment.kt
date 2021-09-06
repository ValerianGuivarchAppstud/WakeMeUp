package com.vguivarc.wakemeup.transport.favoritelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentFavoriteListBinding
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber


class FavoriteListFragment : Fragment(R.layout.fragment_favorite_list),
    FavoriteListAdapter.RecyclerItemClickListener {

    private val viewModel by viewModel<FavoriteListViewModel>()

    private var _binding: FragmentFavoriteListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoriteListSwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.getFavoriteList()
        binding.favoriteListSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: FavoriteListState) {
        Timber.d("render $state")
        binding.loader.isVisible = state.isLoading
        binding.favoriteListSwipeRefreshLayout.isVisible = !state.isLoading
        val list = view?.findViewById<RecyclerView>(R.id.contactList) ?: return
        if (list.adapter == null) {
            list.adapter = FavoriteListAdapter(state.favoriteList, this)
        } else {
            (list.adapter as? FavoriteListAdapter)?.updateData(state.favoriteList)
        }
    }

    private fun handleSideEffect(sideEffect: FavoriteListSideEffect) {
        when (sideEffect) {
            is FavoriteListSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPlayListener(recherche: Favorite, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onShareListener(recherche: Favorite, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeleteListener(recherche: Favorite, position: Int) {
        TODO("Not yet implemented")
    }
}
/*
class FavoriteListFragment :
    BaseLceFragment(R.layout.fragment_favori_list),
    FavoriteAdapter.RecyclerItemClickListener {

    private var favoriteList = mutableListOf<Favorite>()
    private lateinit var favoriteAdapter: FavoriteAdapter

    // private lateinit var youTubePlayerView: YouTubePlayerView
    private val viewModelFavoriteList: FavoriteListViewModel by inject()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.contentView)

        favoriteAdapter = FavoriteAdapter(favoriteList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = favoriteAdapter

        viewModelFavoriteList.getFavoriteList()
        viewModelFavoriteList.favoriteList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Loading -> showLoading()
                    is Success -> {
                        val favoriteList = it.data ?: emptyList()
                        refreshUI(favoriteList)
                        if (favoriteList.isEmpty()) showEmptyView() else showContent()
                    }
                    is Fail -> showError()
                }
            }
        )
        view.findViewById<FloatingActionButton>(R.id.button_add_favorite).setOnClickListener {
            findNavController().navigate(
                R.id.searchVideoFragment
            )
        }
    }

    private fun refreshUI(favoriteList: List<Favorite>) {
        favoriteAdapter.setFavoriteList(favoriteList)
        favoriteAdapter.notifyDataSetChanged()
    }


    override fun onPlayListener(recherche: Favorite, position: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + recherche.song!!.id)
            )
        )
    }

    override fun onShareListener(recherche: Favorite, position: Int) {
        /*  val action =
              com.vguivarc.wakemeup.song.favori.FavorisFragmentDirections.actionFavorisFragmentToContactsListeShareFragment(
                  recherche.song!!
              )
          findNavController().navigate(action)*/
    }

    override fun onDeleteListener(recherche: Favorite, position: Int) {
        AlertDialog.Builder(requireContext()).setTitle("Supprimer favori")
            .setMessage("Voulez-vous supprimer \"" + recherche.song!!.title + "\" de vos favoris ?")
            .setPositiveButton("Supprimer") { _, _ ->
                viewModelFavoriteList.saveFavoriteStatus(recherche, false)
            }
            .setNeutralButton("Annuler") { _, _ -> }.create().show()
    }
}
*/