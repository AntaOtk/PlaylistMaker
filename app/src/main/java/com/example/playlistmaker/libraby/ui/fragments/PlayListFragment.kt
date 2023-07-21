package com.example.playlistmaker.libraby.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import com.example.playlistmaker.libraby.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()

    companion object {
        fun newInstance() = PlayListFragment().apply {
        }
    }

    private lateinit var binding: PlaylistsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
