package com.example.playlistmaker.media.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media.ui.state.FavoriteTracksState
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment(), TrackAdapter.TrackClickListener {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val adapter = TrackAdapter(this)
    private lateinit var rvTracks: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track.trackId)
            )
        }
        rvTracks = binding.favoriteTracksRecyclerView
        rvTracks.adapter = adapter

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showContent(state.tracks)
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        rvTracks.visibility = View.GONE
        binding.mediaIsEmptyLayout.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        rvTracks.visibility = View.VISIBLE
        binding.mediaIsEmptyLayout.visibility = View.GONE

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onTrackClick(track: Track) {
        onTrackClickDebounce(track)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}