package com.example.playlistmaker.creating_playlist.domain

import android.net.Uri
import java.io.File

interface PlaylistCoverRepository {
    fun saveImageToPrivateStorage(uri: Uri, fileName: String)

    fun loadImageFromPrivateStorage(fileName: String) : Uri

    fun setFilePath(filePath: File)
}