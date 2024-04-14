package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    companion object {
//        private const val POSTER_URL = "poster_url"

        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {
//                putString(POSTER_URL, posterUrl)
            }
        }
    }

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private lateinit var binding: FragmentFavoriteTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteTracksLiveData().observe(viewLifecycleOwner) {
            showTracks(it)
        }
    }

    private fun showTracks(tracks: ArrayList<Track>) {
        if (tracks.isEmpty()) binding.mediaIsEmptyLayout.visibility = View.VISIBLE
    }
}