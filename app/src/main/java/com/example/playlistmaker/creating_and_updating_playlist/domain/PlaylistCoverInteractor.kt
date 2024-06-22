package com.example.playlistmaker.creating_and_updating_playlist.domain

import android.net.Uri
import java.io.File

interface PlaylistCoverInteractor {

    fun saveImageToPrivateStorage(uri: Uri, fileName: String)

    fun loadImageFromPrivateStorage(fileName: String) : Uri

    fun deleteImageFromPrivateStorage(fileName: String)

    fun setFilePath(filePath: File)
}