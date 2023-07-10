package com.example.playlistmaker.sharing.domain.impl


import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImp(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.message)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf(context.getString(R.string.mail_to_support)),
            context.getString(R.string.mail_subject),
            context.getString(R.string.mail_subject)
        )
    }

    private fun getTermsLink(): String {

        return context.getString(R.string.offer)
    }
}
