package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    val context: Context
) :
    SharingRepository {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlayList() {
        externalNavigator.shareLink(getShareAppLink())
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