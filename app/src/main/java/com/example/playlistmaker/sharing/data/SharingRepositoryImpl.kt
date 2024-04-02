package com.example.playlistmaker.sharing.data

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.models.EmailData

class SharingRepositoryImpl(private val context: Context): SharingRepository {

    override fun getShareAppLink(): String {
        return getString(context, R.string.link_YP_android)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(getString(context, R.string.email)),
            getString(context, R.string.subject_YP),
            getString(context, R.string.message_YP)
        )
    }

    override fun getTermsLink(): String {
        return getString(context, R.string.link_YP_terms_of_use)
    }
}