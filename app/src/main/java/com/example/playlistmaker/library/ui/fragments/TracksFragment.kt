package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.TracksFragmentBinding
import com.example.playlistmaker.library.ui.FavoriteState
import com.example.playlistmaker.library.ui.adapter.FavoriteAdapter
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.player.ui.activity.AudioPlayer
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {

    private val viewModel by viewModel<TracksViewModel>()

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
        fun newInstance() = TracksFragment().apply {
        }
    }

    private var binding: TracksFragmentBinding? = null
    private val tracks = mutableListOf<Track>()
    private var adapter = FavoriteAdapter(tracks) {
        if (clickDebounce()) {
            AudioPlayer.startActivity(requireContext(), it)
        }
    }
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TracksFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.rvFavorite.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fill()
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding!!.messageText.visibility = View.VISIBLE
        binding!!.messageImage.visibility = View.VISIBLE
        binding!!.rvFavorite.visibility = View.GONE

    }

    private fun showContent(trackList: List<Track>) {
        binding!!.messageText.visibility = View.GONE
        binding!!.messageImage.visibility = View.GONE
        binding!!.rvFavorite.visibility = View.VISIBLE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }
}

