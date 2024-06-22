package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.playlists.domain.PlaylistsInteractor
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import com.example.playlistmaker.playlist.ui.state.PlaylistsTracksState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val playlistId: Int,
    private val simpleDateFormat: SimpleDateFormat,
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private lateinit var playlist: Playlist

    private val playlistLiveData = MutableLiveData<Playlist>()
    private val playlistDurationLiveData = MutableLiveData<String>()
    private val playlistsTracksStateLiveData = MutableLiveData<PlaylistsTracksState>()

    init {
        onResume()
    }

    fun onResume() {
        viewModelScope.launch {
            loadPlaylist().await()
            loadTracks()
        }
    }

    fun getPlaylistLiveData(): LiveData<Playlist> = playlistLiveData
    fun getPlaylistDurationLiveData(): LiveData<String> = playlistDurationLiveData
    fun getTrackListLiveData(): LiveData<PlaylistsTracksState> = playlistsTracksStateLiveData

    fun sharePlaylist(tracks: List<Track>, trackCount: String) {
        var shareString = "${playlist.playlistName}\n\n${playlist.playlistDescription}\n${trackCount}\n"
        for (i in tracks.indices) {
            val track = tracks[i]
            shareString += "\n${i+1}. ${track.artistName}" +
                    " - ${track.trackName}(${simpleDateFormat.format(track.trackTimeMillis)})"
        }
        sharingInteractor.sharePlaylist(shareString)
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(playlist, track)
            onResume()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlist)
        }
    }

    private fun loadPlaylist() : Deferred<Unit> {
        return viewModelScope.async {
            val loadedPlaylist = playlistsInteractor.getPlaylistById(playlistId)
            playlist = loadedPlaylist
            playlistLiveData.postValue(loadedPlaylist)
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            var durationSum: Long = 0
            playlistsInteractor
                .getPlaylistsTracks(playlist.trackIdsList)
                .collect { tracks ->
                    tracks.forEach {
                        durationSum += it.trackTimeMillis
                    }
                    playlistDurationLiveData.postValue(
                        SimpleDateFormat("m", Locale.getDefault()).format(durationSum)
                    )
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) renderState(PlaylistsTracksState.Empty(0))
        else renderState(PlaylistsTracksState.Content(tracks))
    }

    private fun renderState(state: PlaylistsTracksState) {
        playlistsTracksStateLiveData.postValue(state)
    }

}