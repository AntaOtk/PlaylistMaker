package com.example.playlistmaker.playlist.ui

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.main.ui.MainActivity
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.SearchAdapter
import com.example.playlistmaker.search.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlayListFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
        private const val ARGS_PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(ARGS_PLAYLIST_ID to playlistId)

    }

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!
    private val tracks = mutableListOf<Track>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    lateinit var playlist: PlayList
    private val adapter = SearchAdapter(tracks) { track ->
        onTrackClickDebounce(track);
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTrack.adapter = adapter
        val playListId = requireArguments().getLong(ARGS_PLAYLIST_ID)
        viewModel.observeState().observe(viewLifecycleOwner) {
            this.playlist = it
            render(it)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) {
            showContent(it)
        }
        val trackSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }

                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        binding.menuButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showMenu(playlist)
        }

        val backAlertDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.back_alert_title)
            .setNegativeButton(R.string.negative_button) { _, _ ->
            }
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->

            }
        viewModel.getPlayList(playListId)
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            hostViewModel.setCurrentTrack(track)
            findNavController().navigate(
                R.id.action_playListFragment_to_audioPlayer
            )
        }

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

    }

    private fun render(playlist: PlayList) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBOM
            )
        val file = File(filePath, "${playlist.id}.jpg")
        Glide.with(requireActivity())
            .load(file.toUri().toString())
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop()
            )
            .into(binding.cover)
        binding.title.text = playlist.name
        binding.description.text = playlist.description
        val timeText = viewModel.getTracksTime(playlist.tracks)
        binding.tracksTime.text = requireActivity().resources.getQuantityString(
            R.plurals.minutes,
            timeText, timeText
        )
        val countText = requireActivity().resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        )
        binding.trackCount.text = countText
        viewModel.getTracks()
    }

    private fun showContent(trackList: List<Track>) {
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    fun showMenu(playlist: PlayList) {
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBOM
            )
        val file = File(filePath, "${playlist.id}.jpg")
        Glide.with(requireActivity())
            .load(file.toUri().toString())
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop()
            )
            .into(binding.plImage)
        binding.plName.text = playlist.name
        val countText = requireActivity().resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        )
        binding.plCount.text = countText
    }
}