package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }


    private fun getShareAppLink(): String {
        return sharingRepository.getShareAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return sharingRepository.getSupportEmailData()
    }

    private fun getTermsLink(): String {
        return sharingRepository.getTermsLink()
    }
}