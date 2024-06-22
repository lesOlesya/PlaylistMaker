package com.example.playlistmaker.creating_and_updating_playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.example.playlistmaker.creating_and_updating_playlist.domain.PlaylistCoverRepository
import java.io.File
import java.io.FileOutputStream

class PlaylistCoverRepositoryImpl(context: Context) : PlaylistCoverRepository {

    private lateinit var filePath: File // getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

    private val contentResolver = context.contentResolver

    override fun saveImageToPrivateStorage(uri: Uri, fileName: String) {
        val file = File(filePath, fileName) // экземпляр класса File, который указывает на файл внутри каталога
        val inputStream = contentResolver.openInputStream(uri) // создаём входящий поток байтов из выбранной картинки
        val outputStream = FileOutputStream(file) // создаём исходящий поток байтов в созданный выше файл
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun loadImageFromPrivateStorage(fileName: String): Uri {
        val file = File(filePath, fileName)
        return file.toUri()
    }

    override fun deleteImageFromPrivateStorage(fileName: String) {
        val file = File(filePath, fileName)
        file.delete()
    }

    override fun setFilePath(filePath: File) {
        this.filePath = filePath
    }
}