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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val chooserIntent = Intent.createChooser(intent, null)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(context, chooserIntent, null)
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, supportEmailData.email)
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, this, null)
        }
    }

    override fun openLink(termsLink: String) {
        val url = Uri.parse(termsLink)
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }

}