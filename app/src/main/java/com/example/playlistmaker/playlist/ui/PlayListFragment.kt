package com.example.playlistmaker.playlist.ui

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.doOnNextLayout
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
    }

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!
    private val tracks = mutableListOf<Track>()
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    lateinit var playlist: PlayList
    private lateinit var adapter: TracksInPlayListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUpdatePlayList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = hostViewModel.getPlayList().value!!
        viewModel.getCurrentPlayList(playlist)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) {
            showContent(it)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.rvTrack.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.rvTrack.visibility = View.GONE
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
        binding.shareButton.setOnClickListener { share() }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun share() {
        if (playlist.trackCount > 0) viewModel.sharePlayList(playlist)
        else showMistakeDialog()
    }

    private fun showMistakeDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setMessage(R.string.share_mistake)
            .setNeutralButton(
                R.string.ok
            ) { _, _ -> }.show()

    }

    private fun render(playlist: PlayList) {
        adapter = TracksInPlayListAdapter(tracks,
            MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.AlertDialogTheme
            ).setTitle(R.string.delete_track_alert_title)
                .setMessage(R.string.delete_track_alert_message),
            { track ->
                onTrackClickDebounce(track)
            },
            { track ->
                viewModel.removeTrack(track, playlist)
            })
        binding.rvTrack.adapter = adapter
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
        binding.tracksBottomSheet.doOnNextLayout {
            val openMenuLocation = IntArray(2)
            binding.shareButton.getLocationOnScreen(openMenuLocation)

            val openMenuHeightFromBottom =
                binding.root.height - openMenuLocation[1] - resources.getDimensionPixelSize(R.dimen.margin_item_track)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
            bottomSheetBehavior.peekHeight = openMenuHeightFromBottom
        }
    }

    private fun showContent(trackList: List<Track>) {
        tracks.clear()
        tracks.addAll(trackList.reversed())
        adapter.notifyDataSetChanged()
        if (trackList.isEmpty()) showMistakeDialog()

    }

    private fun showMenu(playlist: PlayList) {
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
            .into(binding.item.plImage)
        binding.item.plName.text = playlist.name
        val countText = requireActivity().resources.getQuantityString(
            R.plurals.tracksContOfList,
            playlist.trackCount.toInt(), playlist.trackCount
        )
        binding.item.plCount.text = countText

        binding.deleteTextMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setMessage(
                    requireActivity().resources.getString(R.string.delete_playlist_alert_title)
                        .format(playlist.name)
                )
                .setNegativeButton(R.string.negative_button) { _, _ ->
                }
                .setPositiveButton(R.string.yes_button) { _, _ ->
                    findNavController().navigateUp()
                    viewModel.deletePlayList(playlist)
                }.show()
        }
        binding.shareTextMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
            viewModel.sharePlayList(playlist)
        }
        binding.updateTextMenu.setOnClickListener {
            hostViewModel.setPlayList(playlist)
            findNavController().navigate(R.id.action_playListFragment_to_playlistEditorFragment)
        }
    }

}