package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.view_model.PlaylistLibraryViewModel
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private val playlistsViewModel by viewModel<PlaylistLibraryViewModel>()
    private val tracksViewModel by viewModel<TracksViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(darkTheme = isSystemInDarkTheme()) {
                    MediaLibraryScreen(
                        tracksViewModel = tracksViewModel,
                        playListsViewModel = playlistsViewModel,
                        onTrackClick = { track ->
                            hostViewModel.setCurrentTrack(track)
                            findNavController().navigate(R.id.action_libraryFragment_to_audioPlayer)
                        },
                        onPlaylistClick = { playList ->
                            hostViewModel.setPlayList(playList)
                            findNavController().navigate(
                                R.id.action_libraryFragment_to_playListFragment,
                            )

                        },
                        onNewPlaylistClick = {
                            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreatorFragment)
                        })
                }
            }
        }
    }
}
