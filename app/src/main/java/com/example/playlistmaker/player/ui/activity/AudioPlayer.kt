package com.example.playlistmaker.player.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : Fragment() {

    private val viewModel by viewModel<PlayerViewModel>()

    companion object {
        const val TRACK = "TRACK"

        fun createArgs(track: Track): Bundle =
            bundleOf(TRACK to track)
    }

    private var _binding: AudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val playlists = mutableListOf<PlayList>()
    lateinit var track: Track
    private val adapter = SmallPlayListAdapter(playlists) {
        viewModel.addToPlaylist(track, it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        track = requireArguments().getParcelable(TRACK, Track::class.java)!!

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
        binding.playButton.setOnClickListener { viewModel.playbackControl() }
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeProgressTimeState().observe(viewLifecycleOwner) {
            progressTimeViewUpdate(it)
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

        binding.likeButton.setOnClickListener { viewModel.onFavoriteClicked(track) }
        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }



        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate((R.id.action_audioPlayer_to_playlistCreatorFragment))
        }

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

        viewModel.prepare(track)
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
        when (state) {
            PlayerState.PLAYING -> startPlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> pausePlayer()
        }
    }

    private fun startPlayer() {
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun progressTimeViewUpdate(progressTime: String) {
        binding.progressTime.text = progressTime
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}
