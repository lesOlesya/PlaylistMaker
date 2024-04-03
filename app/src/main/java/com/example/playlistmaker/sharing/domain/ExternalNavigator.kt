package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.models.EmailData

interface ExternalNavigator {

    fun shareLink(appLink: String)

    fun openEmail(supportEmailData: EmailData)

    fun openLink(termsLink: String)
}