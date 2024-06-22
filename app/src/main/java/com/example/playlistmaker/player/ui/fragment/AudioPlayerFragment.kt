package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.media.playlists.ui.state.PlaylistsState
import com.example.playlistmaker.player.ui.adapter.PlaylistHorizontalAdapter
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.util.debounce
import com.example.playlistmaker.util.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat


class AudioPlayerFragment : Fragment(), PlaylistHorizontalAdapter.PlaylistClickListener {

    private lateinit var binding: FragmentAudioPlayerBinding

    private lateinit var viewModel : PlayerViewModel

    private val simpleDateFormat: SimpleDateFormat by inject()

    private var trackId: Int? = null

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    private val adapter = PlaylistHorizontalAdapter(this)
    private lateinit var rvPlaylists: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackId = it.getInt(ARGS_TRACK_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel<PlayerViewModel> {
            parametersOf(trackId)
        }.value

        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        rvPlaylists = binding.playlistsRecyclerView
        rvPlaylists.adapter = adapter

        binding.audioPlayerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        onPlaylistClickDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
            viewModel.onPlaylistClick(
                playlist,
                listOf(getString(R.string.track_already_added_toast),
                    getString(R.string.added_to_playlist_toast),
                    getString(R.string.error_toast))
            )
        }

        viewModel.getTrackLiveData().observe(viewLifecycleOwner) { track ->
            binding.trackNameTvAudioPlayer.text = track.trackName
            binding.artistNameTvAudioPlayer.text = track.artistName
            binding.durationTvAudioPlayer.text =
                simpleDateFormat.format(track.trackTimeMillis)!!
            binding.albumTvAudioPlayer.text = track.collectionName.orEmpty()
            binding.yearTvAudioPlayer.text = track.releaseDate?.substring(0, 4).orEmpty()
            binding.genreTvAudioPlayer.text = track.primaryGenreName.orEmpty()
            binding.countryTvAudioPlayer.text = track.country.orEmpty()
            binding.favoritesButton.isChecked = track.isFavorite
            Glide.with(this)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8F, binding.artworkIvAudioPlayer.context)))
                .into(binding.artworkIvAudioPlayer)
        }

        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { playStatus ->
            binding.playButton.isChecked = playStatus.isPlaying
            binding.trackTimeTvAudioPlayer.text = playStatus.progress
        }

        binding.playButton.setOnClickListener {
            viewModel.play(getString(R.string.snippet_not_loaded_toast))
        }

        binding.favoritesButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.buttonNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_audioPlayerFragment_to_creatingPlaylistFragment)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> { // full opened
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> { // opened
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 0.4F
            }
        })

        viewModel.getIsFavoriteLiveData().observe(viewLifecycleOwner) {
            binding.favoritesButton.isChecked = it
        }

        viewModel.getToastLiveData().observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT, ).show()
            }
        })

        viewModel.getPlaylistsStateLiveData().observe(viewLifecycleOwner) {
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

    override fun onPlaylistClick(playlist: Playlist) {
        onPlaylistClickDebounce(playlist)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ARGS_TRACK_ID = "TrackId"

        fun createArgs(trackId: Int): Bundle =
            bundleOf(ARGS_TRACK_ID to trackId)
    }
}