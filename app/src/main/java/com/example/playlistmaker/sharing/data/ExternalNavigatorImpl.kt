package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.models.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink(appLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, appLink)
        startActivity(context, Intent.createChooser(intent, null), null)
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, supportEmailData.email)
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
            startActivity(context,this, null)
        }
    }

    override fun openLink(termsLink: String) {
        val url = Uri.parse(termsLink)
        val intent = Intent(Intent.ACTION_VIEW, url)
        startActivity(context, intent, null)
    }

}