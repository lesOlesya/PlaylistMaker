package com.example.playlistmaker.creating_playlist.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.media.domain.PlaylistsInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch
import java.io.File

class CreatingPlaylistViewModel(
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val coverUriLiveData = MutableLiveData<Uri>()
    fun getCoverUriLiveData(): LiveData<Uri> = coverUriLiveData


    fun createPlaylist(playlistName: String, playlistDescription: String) {
        val playlistId = System.currentTimeMillis()
        coverUriLiveData.value?.let {
            saveCover(it, playlistId.toString())
        }
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    playlistId,
                    playlistName,
                    playlistDescription,
                    playlistCoverInteractor.loadImageFromPrivateStorage(playlistId.toString()).toString(),
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
        coverUriLiveData.postValue(uri)
    }

    private fun saveCover(uri: Uri, fileName: String) {
        playlistCoverInteractor.saveImageToPrivateStorage(uri, fileName)
    }

}