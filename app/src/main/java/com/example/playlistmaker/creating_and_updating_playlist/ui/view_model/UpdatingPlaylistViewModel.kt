package com.example.playlistmaker.creating_and_updating_playlist.ui.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.creating_and_updating_playlist.ui.state.UpdatingPlaylistState
import com.example.playlistmaker.media.playlists.domain.PlaylistsInteractor
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.launch

class UpdatingPlaylistViewModel(
    private val playlistId: Int,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : CreatingPlaylistViewModel(playlistCoverInteractor, playlistsInteractor) {

    private lateinit var playlist: Playlist
    private var oldCoverUri: String? = null

    private val updatingStateLiveData = MutableLiveData<UpdatingPlaylistState>()

    init {
        loadPlaylist()
    }

    fun getUpdatingStateLiveData(): LiveData<UpdatingPlaylistState> = updatingStateLiveData

    private fun loadPlaylist() {
        viewModelScope.launch {
            val loadedPlaylist = playlistsInteractor.getPlaylistById(playlistId)
            playlist = loadedPlaylist
            oldCoverUri = loadedPlaylist.coverUri
            coverUriLiveData.postValue(oldCoverUri)
            processResult(playlist)
        }
    }

    override fun createPlaylist(playlistName: String, playlistDescription: String) {
        var timeOfUpdate = playlist.timeOfUpdate
        if (oldCoverUri != coverUriLiveData.value) {
            deleteOldCover(timeOfUpdate.toString())
            timeOfUpdate = System.currentTimeMillis()
            coverUriLiveData.value?.let {
                saveCover(it.toUri(), timeOfUpdate.toString())
            }
        }
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    playlistId,
                    timeOfUpdate,
                    playlistName,
                    playlistDescription,
                    playlistCoverInteractor.loadImageFromPrivateStorage(timeOfUpdate.toString()).toString(),
                    playlist.trackIdsList,
                    playlist.tracksCount
                )
            )
        }
    }

    private fun deleteOldCover(fileName: String) {
        playlistCoverInteractor.deleteImageFromPrivateStorage(fileName)
    }

    private fun processResult(playlist: Playlist) {
        renderState(UpdatingPlaylistState.Content(
                        playlist.coverUri,
                        playlist.playlistName,
                        playlist.playlistDescription
                    ))
    }

    private fun renderState(state: UpdatingPlaylistState) {
        updatingStateLiveData.postValue(state)
    }
}