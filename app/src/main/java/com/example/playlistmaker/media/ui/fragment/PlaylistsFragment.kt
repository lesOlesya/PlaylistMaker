package com.example.playlistmaker.media.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.adapter.PlaylistAdapter
import com.example.playlistmaker.media.ui.state.PlaylistsState
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistAdapter.PlaylistClickListener {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: FragmentPlaylistsBinding

//    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private val adapter = PlaylistAdapter(this)
    private lateinit var rvPlaylists: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_creatingPlaylistFragment)
        }

//        onPlaylistClickDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
//            findNavController().navigate(
//                R.id.action_mediaFragment_to_playlistFragment,
//                PlaylistFragment.newInstance(playlist.playlistId)
//            )
//        }

        rvPlaylists = binding.playlistsRecyclerView
        rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        rvPlaylists.adapter = adapter

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        rvPlaylists.visibility = View.GONE
        binding.playlistListIsEmptyLayout.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(playlists: List<Playlist>) {
        rvPlaylists.visibility = View.VISIBLE
        binding.playlistListIsEmptyLayout.visibility = View.GONE

        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPlaylistClick(playlist: Playlist) {
//        onPlaylistClickDebounce(playlist)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}