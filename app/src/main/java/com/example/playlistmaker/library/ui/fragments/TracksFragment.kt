package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TracksFragmentBinding
import com.example.playlistmaker.library.ui.FavoriteState
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.adapter.SearchAdapter
import com.example.playlistmaker.search.util.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {

    private val viewModel by viewModel<TracksViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var _binding: TracksFragmentBinding? = null

    private val binding get() = _binding!!
    private val tracks = mutableListOf<Track>()
    private var adapter = SearchAdapter(tracks) {
        onTrackClickDebounce(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavorite.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            hostViewModel.setCurrentTrack(track)
            findNavController().navigate(R.id.action_libraryFragment_to_audioPlayer)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fill()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.messageText.visibility = View.VISIBLE
        binding.messageImage.visibility = View.VISIBLE
        binding.rvFavorite.visibility = View.GONE

    }

    private fun showContent(trackList: List<Track>) {
        binding.messageText.visibility = View.GONE
        binding.messageImage.visibility = View.GONE
        binding.rvFavorite.visibility = View.VISIBLE
        tracks.clear()
        tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
        fun newInstance() = TracksFragment()
    }
}

