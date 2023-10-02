package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.library.ui.PlaylistsState
import com.example.playlistmaker.library.ui.adapter.PlayListAdapter
import com.example.playlistmaker.library.ui.view_model.PlaylistLibraryViewModel
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.search.util.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListLibraryFragment : Fragment() {

    private val viewModel by viewModel<PlaylistLibraryViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
        fun newInstance() = PlayListLibraryFragment()
    }

    private var _binding: PlaylistsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (PlayList) -> Unit
    private val playlists = mutableListOf<PlayList>()
    private var adapter = PlayListAdapter(playlists) {
        onTrackClickDebounce(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPlaylist.adapter = adapter
        binding.rvPlaylist.layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        viewModel.fill()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreatorFragment)
        }

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playList ->
            hostViewModel.setPlayList(playList)
            findNavController().navigate(
                R.id.action_libraryFragment_to_playListFragment,
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fill()
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.items)
            is PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.messageText.visibility = View.VISIBLE
        binding.messageImage.visibility = View.VISIBLE
        binding.rvPlaylist.visibility = View.GONE

    }

    private fun showContent(items: List<PlayList>) {
        binding.messageText.visibility = View.GONE
        binding.messageImage.visibility = View.GONE
        binding.rvPlaylist.visibility = View.VISIBLE
        playlists.clear()
        playlists.addAll(items)
        adapter.notifyDataSetChanged()
    }
}
