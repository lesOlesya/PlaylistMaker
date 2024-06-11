package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistsInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.state.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private var playlistsInteractor : PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun getStateLiveData(): LiveData<PlaylistsState> = stateLiveData

    init {
        onResume()
    }

    fun onResume() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect { it ->
                    processResult(it)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) renderState(PlaylistsState.Empty(0))
        else renderState(PlaylistsState.Content(playlists))
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }
}