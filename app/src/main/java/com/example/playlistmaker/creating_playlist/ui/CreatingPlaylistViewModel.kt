package com.example.playlistmaker.creating_playlist.ui

import android.net.Uri
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

    var uri: Uri? = null

    fun createPlaylist(playlistName: String, playlistDescription: String) {
        uri?.let { saveCover(uri!!, playlistName) }
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    System.currentTimeMillis(),
                    playlistName,
                    playlistDescription,
                    playlistCoverInteractor.loadImageFromPrivateStorage(playlistName).toString(),
                    ArrayList<Int>(),
                    0
                    )
            )
        }
    }

    fun setFilePath(filePath: File) {
        playlistCoverInteractor.setFilePath(filePath)
    }

    private fun saveCover(uri: Uri, fileName: String) {
        playlistCoverInteractor.saveImageToPrivateStorage(uri, fileName)
    }

}