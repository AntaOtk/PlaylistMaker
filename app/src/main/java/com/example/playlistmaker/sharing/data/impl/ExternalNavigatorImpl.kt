package com.example.playlistmaker.sharing.data.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl : ExternalNavigator {
    override fun shareLink(shareAppLink: String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        intent.type = "text/plain"
    }
    override fun openLink(termsLink: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(termsLink)
    }
    override fun openEmail(supportEmailData: EmailData){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL,supportEmailData.emailAddressee)
        intent.putExtra(Intent.EXTRA_SUBJECT,supportEmailData.emailTopic)
        intent.putExtra(Intent.EXTRA_TEXT, supportEmailData.emailMessage)
    }

}