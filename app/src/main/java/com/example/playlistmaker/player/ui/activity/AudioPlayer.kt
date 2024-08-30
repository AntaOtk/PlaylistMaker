package com.example.playlistmaker.player.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.services.MediaPlayerService
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.ConnectionBroadcastReceiver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : Fragment() {

    private val viewModel by viewModel<PlayerViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()
    private val connectionBroadcastReceiver = ConnectionBroadcastReceiver()
    private var _binding: AudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val playlists = mutableListOf<PlayList>()
    var track: Track? = null
    private val adapter = SmallPlayListAdapter(playlists) {
        track?.let { it1 -> viewModel.addToPlaylist(it1, it) }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MusicServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            connectionBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        viewModel.hideNotification()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hostViewModel.getCurrentTrack().observe(viewLifecycleOwner) { currentTrack ->
            this.track = currentTrack
            renderInformation(currentTrack)
            viewModel.getChecked(currentTrack)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                bindMusicService()
            }
        }
        binding.playButton.onTouchListener = { viewModel.onPlayerButtonClicked() }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        viewModel.renderPlayLists()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        binding.recyclerView.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            favoriteRender(it)
        }
        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlayList(it)
        }
        viewModel.observeAddDtate().observe(viewLifecycleOwner) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showToast(it)
        }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.likeButton.setOnClickListener { track?.let { it1 -> viewModel.onFavoriteClicked(it1) } }
        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate((R.id.action_audioPlayer_to_playlistCreatorFragment))
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun bindMusicService() {
        val track = hostViewModel.getCurrentTrack().value
        val intent = Intent(requireContext(), MediaPlayerService::class.java).apply {
            putExtra(TRACK_URL, track?.previewUrl)
            putExtra(TRACK_TITLE, track?.trackName)
            putExtra(TRACK_ARTIST, track?.artistName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    private fun renderInformation(track: Track) {
        binding.title.text = track.trackName
        binding.artist.text = track.artistName
        binding.albumName.text = track.collectionName
        binding.year.text = track.releaseDate.substring(0, 4)
        binding.styleName.text = track.primaryGenreName
        binding.countryName.text = track.country
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide.with(requireActivity())
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art)))
            .into(binding.cover)
    }

    private fun renderPlayList(list: List<PlayList>) {
        playlists.clear()
        playlists.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun favoriteRender(favoriteChecked: Boolean) {
        if (favoriteChecked)
            binding.likeButton.setImageResource(R.drawable.like_button_on)
        else binding.likeButton.setImageResource(R.drawable.like_button_off)
    }

    private fun render(state: PlayerState) {
        binding.playButton.changeButtonStatus(state.buttonState)
        progressTimeViewUpdate(state.progress)
    }

    private fun progressTimeViewUpdate(progressTime: String) {
        binding.progressTime.text = progressTime
    }

    private fun showToast(result: Pair<String, Boolean>) {
        val message =
            if (result.second) getString(R.string.add_track_message) else getString(R.string.add_track_message_false)
        Toast.makeText(requireContext(), message + " " + result.first, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(connectionBroadcastReceiver)
        viewModel.showNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
    }

    companion object {
        const val TRACK_URL = "song_url"
        const val TRACK_TITLE = "song_title"
        const val TRACK_ARTIST = "song_artist"

    }
}
