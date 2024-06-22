package com.example.playlistmaker.creating_and_updating_playlist.domain.impl

import android.net.Uri
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverInteractor
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverRepository
import java.io.File

class PlaylistCoverInteractorImpl(private val playlistCoverRepository: PlaylistCoverRepository) :
    PlaylistCoverInteractor {
    override fun saveImageToPrivateStorage(uri: Uri, fileName: String) {
        return playlistCoverRepository.saveImageToPrivateStorage(uri, fileName)
    }

    override fun loadImageFromPrivateStorage(fileName: String): Uri {
        return playlistCoverRepository.loadImageFromPrivateStorage(fileName)
    }

    override fun deleteImageFromPrivateStorage(fileName: String) {
        return playlistCoverRepository.deleteImageFromPrivateStorage(fileName)
    }

    override fun setFilePath(filePath: File) {
        return playlistCoverRepository.setFilePath(filePath)
    }
}