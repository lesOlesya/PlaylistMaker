package com.example.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creating_and_updating_playlist.ui.fragment.UpdatingPlaylistFragment
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.playlist.ui.state.PlaylistsTracksState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapter.TrackAdapter
import com.example.playlistmaker.util.debounce
import com.example.playlistmaker.util.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistInfoFragment : Fragment(), TrackAdapter.TrackClickListener {

    private lateinit var binding: FragmentPlaylistInfoBinding

    private lateinit var viewModel : PlaylistInfoViewModel

    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<ConstraintLayout>

    private var playlistId: Int? = null

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onEditClickDebounce: (Playlist) -> Unit
    private lateinit var deleteTrackLongClick: (Track) -> Unit

    private val adapter = TrackAdapter(this)
    private lateinit var rvTracks: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getInt(ARGS_PLAYLIST_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel<PlaylistInfoViewModel> {
            parametersOf(playlistId)
        }.value

        setBottomSheets()

        rvTracks = binding.tracksRecyclerView
        rvTracks.adapter = adapter

        binding.playlistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        onTrackClickDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            findNavController().navigate(
                R.id.action_playlistInfoFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track.trackId)
            )
        }

        onEditClickDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { playlist ->
            findNavController().navigate(
                R.id.action_playlistInfoFragment_to_updatingPlaylistFragment,
                UpdatingPlaylistFragment.createArgs(playlist.playlistId)
            )
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }

        deleteTrackLongClick = { track ->
            confirmDialog
                .setMessage(getString(R.string.delete_track_dialog_title))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.deleteTrack(track)
                }
                .show()
        }

        binding.deletePlaylistBsTv.setOnClickListener {
            confirmDialog
                .setMessage(getString(R.string.delete_playlist_dialog_title)
                    .format(viewModel.getPlaylistLiveData().value?.playlistName ?: ""))
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }
                .show()
        }

        binding.shareBsTv.setOnClickListener {
            sharePlaylist()
        }

        binding.editInfoBsTv.setOnClickListener {
            viewModel.getPlaylistLiveData().value?.let { onEditClickDebounce(it) }
        }

        binding.shareIv.setOnClickListener {
            sharePlaylist()
        }

        binding.menuIv.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlist ->
            binding.playlistName.text = playlist.playlistName
            if (playlist.playlistDescription.isNullOrEmpty())
                binding.playlistDescription.isVisible = false
            else binding.playlistDescription.text = playlist.playlistDescription
            binding.tracksCount.text = this.resources.getQuantityString(R.plurals.trackCount,
                playlist.tracksCount, playlist.tracksCount)

            Glide.with(this)
                .load(playlist.coverUri)
                .placeholder(R.drawable.placeholder_playlist)
                .transform(CenterCrop(), RoundedCorners(dpToPx(8F, binding.playlistCoverIv.context)))
                .into(binding.playlistCoverIv)

            binding.playlistNameBottomSheet.text = playlist.playlistName
            binding.tracksCountBottomSheet.text = this.resources.getQuantityString(R.plurals.trackCount,
                playlist.tracksCount, playlist.tracksCount)

            Glide.with(this)
                .load(playlist.coverUri)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(dpToPx(2F, binding.playlistSmallCoverBottomSheet.context)))
                .into(binding.playlistSmallCoverBottomSheet)
        }

        viewModel.getPlaylistDurationLiveData().observe(viewLifecycleOwner) {
            binding.playlistDuration.text = this.resources.getQuantityString(R.plurals.minuteCount,
                it.toInt(), it.toInt())
        }

        viewModel.getTrackListLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistsTracksState) {
        when (state) {
            is PlaylistsTracksState.Content -> showContent(state.tracks)
            is PlaylistsTracksState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        rvTracks.visibility = View.GONE
        binding.playlistsTrackListIsEmpty.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        rvTracks.visibility = View.VISIBLE
        binding.playlistsTrackListIsEmpty.visibility = View.GONE

        adapter.tracks.clear()
        adapter.tracks.addAll(tracks.reversed())
        adapter.notifyDataSetChanged()
    }

    private fun sharePlaylist() {
        if (adapter.tracks.isEmpty()) {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            Toast.makeText(requireContext(), getString(R.string.no_tracks_for_shared), Toast.LENGTH_LONG).show()
        }
        else viewModel.sharePlaylist(adapter.tracks,
            this.resources.getQuantityString(R.plurals.trackCount, adapter.tracks.size, adapter.tracks.size))
    }

    private fun setBottomSheets() {

        val overlay = binding.overlay

        val bottomSheetContainerMenu = binding.menuBottomSheet
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainerMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetContainerMenu.maxHeight = bottomSheetBehaviorMenu.peekHeight

        val bottomSheetContainerTracks = binding.trackListBottomSheet
        val bottomSheetBehaviorTracks = BottomSheetBehavior.from(bottomSheetContainerTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehaviorTracks.peekHeight = this.resources.displayMetrics.heightPixels * 32 / 100

        bottomSheetBehaviorMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bottomSheetContainerTracks.isVisible = false
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                        bottomSheetContainerTracks.isVisible = true
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset + 0.5F
            }
        })
    }

    override fun onTrackClick(track: Track) {
        onTrackClickDebounce(track)
    }

    override fun onTrackLongClick(track: Track) {
        deleteTrackLongClick(track)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ARGS_PLAYLIST_ID = "PlaylistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}