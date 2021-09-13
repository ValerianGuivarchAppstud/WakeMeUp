package com.vguivarc.wakemeup.transport.ringinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentFavoriteListBinding
import com.vguivarc.wakemeup.domain.external.entity.Favorite
import com.vguivarc.wakemeup.transport.search.SearchSongListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class RingingListFragment : Fragment() {

    private val viewModel by viewModel<RingingListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Text("Hello!")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    override fun onResume() {
        super.onResume()
    }
}