package com.example.playlistmaker.creating_and_updating_playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creating_and_updating_playlist.ui.state.UpdatingPlaylistState
import com.example.playlistmaker.creating_and_updating_playlist.ui.view_model.UpdatingPlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatingPlaylistBinding
import com.example.playlistmaker.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UpdatingPlaylistFragment : CreatingPlaylistFragment() {

    override lateinit var viewModel : UpdatingPlaylistViewModel

    private var playlistId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getInt(ARGS_PLAYLIST_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatingPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = viewModel<UpdatingPlaylistViewModel> {
            parametersOf(playlistId)
        }.value

        super.onViewCreated(view, savedInstanceState)

        binding.titleToolbar.text = getString(R.string.edit_playlist)
        createPlaylistButton.text = getString(R.string.to_save_button_text)

        createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist(
                playlistNameEditText.text.toString(),
                playlistDescriptionEditText.text.toString()
            )
            findNavController().navigateUp()
            Toast.makeText(requireContext(),
                getString(R.string.playlist_updated_toast), Toast.LENGTH_LONG).show()
        }

        viewModel.getUpdatingStateLiveData().observe(viewLifecycleOwner) {state ->
            if (state is UpdatingPlaylistState.Content) {
                Glide.with(this)
                    .load(state.playlistCoverUri)
                    .placeholder(R.drawable.add_cover_placeholder)
                    .transform(CenterCrop(), RoundedCorners(dpToPx(8F, coverIv.context)))
                    .into(coverIv)
                playlistNameEditText.setText(state.playlistName)
                playlistDescriptionEditText.setText(state.playlistDescription)
            }
        }

    }

    override fun finishOrShowConfirmDialog() {
        findNavController().navigateUp()
    }

    companion object {

        private const val ARGS_PLAYLIST_ID = "PlaylistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }
}