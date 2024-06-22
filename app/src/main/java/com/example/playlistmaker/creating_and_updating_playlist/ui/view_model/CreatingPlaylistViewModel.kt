package com.example.playlistmaker.creating_and_updating_playlist.ui.view_model

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.media.playlists.domain.PlaylistsInteractor
import com.example.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.launch
import java.io.File

open class CreatingPlaylistViewModel(
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    protected val coverUriLiveData = MutableLiveData<String>()
    fun getCoverUriLiveData(): LiveData<String> = coverUriLiveData


    open fun createPlaylist(playlistName: String, playlistDescription: String) {
        val timeOfUpdate = System.currentTimeMillis()
        coverUriLiveData.value?.let {
            saveCover(it.toUri(), timeOfUpdate.toString())
        }
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    0, // autoGenerate
                    timeOfUpdate,
                    playlistName,
                    playlistDescription,
                    playlistCoverInteractor.loadImageFromPrivateStorage(timeOfUpdate.toString()).toString(),
                    ArrayList<Int>(),
                    0
                    )
            )
        }
    }

    fun setFilePath(filePath: File) {
        playlistCoverInteractor.setFilePath(filePath)
    }

    fun setCover(uri: Uri) {
        coverUriLiveData.postValue(uri.toString())
    }

    protected fun saveCover(uri: Uri, fileName: String) {
        playlistCoverInteractor.saveImageToPrivateStorage(uri, fileName)
    }

}