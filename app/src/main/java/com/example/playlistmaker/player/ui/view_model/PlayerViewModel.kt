package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.PlaylistsInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.ui.state.PlaylistsState
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Event
import kotlinx.coroutines.launch

class PlayerViewModel(
    trackId: Int,
    getTrackByIdUseCase: GetTrackByIdUseCase,
    private val trackPlayer: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val track = getTrackByIdUseCase.execute(trackId)
    private val url = track!!.previewUrl

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    private val trackLiveData = MutableLiveData<Track>()
    private val toastLiveData = MutableLiveData<Event<String>>()
    private val playlistsStateLiveData = MutableLiveData<PlaylistsState>()

    init {
        loadPlaylists()
        trackLiveData.postValue(track!!)
        url?.let { trackPlayer.preparePlayer(url) } //if (url != null) trackPlayer.preparePlayer()
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun getIsFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData
    fun getTrackLiveData(): LiveData<Track> = trackLiveData
    fun getToastLiveData(): LiveData<Event<String>> = toastLiveData
    fun getPlaylistsStateLiveData(): LiveData<PlaylistsState> = playlistsStateLiveData

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect { it ->
                    processResult(it)
                }
        }
    }

    fun onPlaylistClick(playlist: Playlist) {
        val trackIds = playlist.tracksList
        trackIds.forEach {
            if (it == track!!.trackId) {
                toastLiveData.value = Event("Трек уже добавлен в плейлист ${playlist.playlistName}")
                return
            }
        }
        viewModelScope.launch {
            val playlistUpdateIsSuccessful = playlistsInteractor.updatePlaylist(playlist, track!!)
            if (playlistUpdateIsSuccessful) {
                loadPlaylists()
                toastLiveData.value = Event("Добавлено в плейлист ${playlist.playlistName}")
            } else toastLiveData.value = Event("Ошибка")
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track!!.isFavorite) favoriteTracksInteractor.deleteTrack(track)
            else favoriteTracksInteractor.addTrack(track)
        }
        track!!.isFavorite = !track.isFavorite
        isFavoriteLiveData.value = track.isFavorite
    }

    fun play() {
        trackPlayer.playbackControl(statusObserver = object : PlayerRepository.StatusObserver {
            override fun onProgress(progress: String) {
                playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
            }

            override fun onStop() {
                playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
            }

            override fun onPlay() {
                playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
            }

            override fun showToast() {
                toastLiveData.value = Event("Отрывок трека не загружен")
            }
        })
    }

    override fun onCleared() {
        trackPlayer.releasePlayer()
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = "00:00", isPlaying = false)
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) renderState(PlaylistsState.Empty(0))
        else renderState(PlaylistsState.Content(playlists))
    }

    private fun renderState(state: PlaylistsState) {
        playlistsStateLiveData.postValue(state)
    }

}