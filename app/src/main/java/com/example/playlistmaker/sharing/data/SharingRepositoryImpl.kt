package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData
import java.text.SimpleDateFormat
import java.util.Locale

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

    override fun sharePlayList(playlist: PlayList) {
        externalNavigator.shareLink(getPlayListString(playlist))
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

    private fun getPlayListString(playlist: PlayList): String {
        var text = playlist.name + "\n"
        text += playlist.description + "\n"
        text += context.resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        ) + "\n"
        for (i in playlist.tracks.indices) {
            val duration = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(playlist.tracks[i].trackTimeMillis)
            text += "${i + 1}. ${playlist.tracks[i].artistName} - ${playlist.tracks[i].trackName}  ($duration)\n"
        }
        return text
    }
}