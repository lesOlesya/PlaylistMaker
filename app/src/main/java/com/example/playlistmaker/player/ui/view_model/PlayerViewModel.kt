package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.models.PlayStatus
import com.example.playlistmaker.search.domain.models.Track

class PlayerViewModel(application: Application, trackId: Int) :
    AndroidViewModel(application) {

    private val getTrackByIdUseCase by lazy { Creator.provideTrackByIdUseCase(getApplication()) }

    private val track = getTrackByIdUseCase.execute(trackId)
    private val url = track!!.previewUrl

    private val trackPlayer = Creator.providePlayerInteractor(
        getApplication(),
        url,
        statusObserver = object : PlayerRepositoryImpl.StatusObserver {
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

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private val trackLiveData = MutableLiveData<Track>()


    init {
        trackLiveData.postValue(track)
        url?.let { trackPlayer.preparePlayer() } //if (url != null) trackPlayer.preparePlayer()
    }

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun getTrackLiveData(): LiveData<Track> = trackLiveData

    fun play() {
        trackPlayer.playbackControl()
    }

    override fun onCleared() {
        trackPlayer.releasePlayer()
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = "00:00", isPlaying = false)
    }

    companion object {
        fun getViewModelFactory(trackId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                PlayerViewModel(context, trackId)
            }
        }
    }

}