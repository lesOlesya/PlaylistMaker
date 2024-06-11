package com.example.playlistmaker.creating_playlist.domain

import android.net.Uri
import java.io.File

class PlaylistCoverInteractorImpl(private val playlistCoverRepository: PlaylistCoverRepository) :
    PlaylistCoverInteractor {
    override fun saveImageToPrivateStorage(uri: Uri, fileName: String) {
        playlistCoverRepository.saveImageToPrivateStorage(uri, fileName)
    }

    override fun loadImageFromPrivateStorage(fileName: String): Uri {
        return playlistCoverRepository.loadImageFromPrivateStorage(fileName)
    }

    override fun setFilePath(filePath: File) {
        playlistCoverRepository.setFilePath(filePath)
    }
}