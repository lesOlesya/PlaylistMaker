package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.media.data.AppDatabase
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

val dataModule = module {

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("playlist_maker_preferences", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    factory { Gson() }

    factory { MediaPlayer() }

    factory { SimpleDateFormat("mm:ss", Locale.getDefault()) }

}