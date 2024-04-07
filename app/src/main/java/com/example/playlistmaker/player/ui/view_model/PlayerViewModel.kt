package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayStatus
import com.example.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    trackId: Int,
    getTrackByIdUseCase: GetTrackByIdUseCase,
    private val trackPlayer: PlayerInteractor
) :
    ViewModel() {

    private val track = getTrackByIdUseCase.execute(trackId)
    private val url = track!!.previewUrl

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val trackLiveData = MutableLiveData<Track>()

    init {
        trackLiveData.postValue(track!!)
        url?.let { trackPlayer.preparePlayer(url) } //if (url != null) trackPlayer.preparePlayer()
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun getTrackLiveData(): LiveData<Track> = trackLiveData

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
        })
    }

    override fun onCleared() {
        trackPlayer.releasePlayer()
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = "00:00", isPlaying = false)
    }

}