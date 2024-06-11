package com.example.playlistmaker.util

import android.app.Application
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class TracksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PermissionRequester.initialize(applicationContext)
        startKoin {
            androidContext(this@TracksApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}
