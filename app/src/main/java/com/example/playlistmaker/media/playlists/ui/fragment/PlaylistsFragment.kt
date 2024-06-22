package com.example.playlistmaker.media.playlists.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.media.playlists.ui.adapter.PlaylistAdapter
import com.example.playlistmaker.media.playlists.ui.state.PlaylistsState
import com.example.playlistmaker.media.playlists.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.playlist.ui.PlaylistInfoFragment
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistAdapter.PlaylistClickListener {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: FragmentPlaylistsBinding

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

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

        onPlaylistClickDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_playlistInfoFragment,
                PlaylistInfoFragment.createArgs(playlist.playlistId)
            )
        }

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

    private fun showContent(playlists: List<Playlist>) {
        rvPlaylists.visibility = View.VISIBLE
        binding.playlistListIsEmptyLayout.visibility = View.GONE

        adapter.submitList(playlists)

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() { // возврат к началу RV после добавления
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                (binding.playlistsRecyclerView.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(positionStart, 0)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPlaylistClick(playlist: Playlist) {
        onPlaylistClickDebounce(playlist)
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