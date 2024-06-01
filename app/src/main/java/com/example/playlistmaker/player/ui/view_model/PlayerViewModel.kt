package com.example.playlistmaker.player.ui.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.GetTrackByIdUseCase
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Event

class PlayerViewModel(
    trackId: Int,
    getTrackByIdUseCase: GetTrackByIdUseCase,
    private val trackPlayer: PlayerInteractor
) : ViewModel() {

    private val track = getTrackByIdUseCase.execute(trackId)
    private val url = track!!.previewUrl

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val trackLiveData = MutableLiveData<Track>()
    private val toastLiveData = MutableLiveData<Event<String>>()

    init {
        trackLiveData.postValue(track!!)
        url?.let { trackPlayer.preparePlayer(url) } //if (url != null) trackPlayer.preparePlayer()
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun getTrackLiveData(): LiveData<Track> = trackLiveData
    fun getToastLiveData(): LiveData<Event<String>> = toastLiveData

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

}