package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.models.EmailData

interface SharingRepository {

    fun getShareAppLink(): String

    fun getSupportEmailData(): EmailData

    fun getTermsLink(): String
}