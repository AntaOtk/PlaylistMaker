package com.example.playlistmaker.libraby.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.TracksFragmentBinding
import com.example.playlistmaker.libraby.ui.viewmodel.TracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {

    private val viewModel by viewModel<TracksViewModel>()

    companion object {
        fun newInstance() = TracksFragment().apply {
        }
    }

    private lateinit var binding: TracksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
