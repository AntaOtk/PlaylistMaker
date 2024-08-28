package com.example.playlistmaker.player.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.player.domain.util.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerService : Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()
    var notificationStatus = false
    private var songUrl = ""
    private var songTitle = ""
    private var songArtist = ""

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(200L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    override fun showNotification(){
        if (_playerState.value is PlayerState.Playing) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                getForegroundServiceTypeConstant()
            )
            notificationStatus = true
        }
    }

    override fun hideNotification(){
        if (notificationStatus){
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            notificationStatus = !notificationStatus
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        createNotificationChannel()

        return binder
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText("$songArtist - $songTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return
        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.value = PlayerState.Finished()
            hideNotification()
        }
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
            ?: "00:00"
    }

    companion object {
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MediaPlayerService = this@MediaPlayerService
    }
}
