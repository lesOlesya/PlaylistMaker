package com.example.playlistmaker.creating_playlist.domain

import android.net.Uri
import java.io.File

interface PlaylistCoverInteractor {

    fun saveImageToPrivateStorage(uri: Uri, fileName: String)

    fun loadImageFromPrivateStorage(fileName: String) : Uri

    fun setFilePath(filePath: File)
}