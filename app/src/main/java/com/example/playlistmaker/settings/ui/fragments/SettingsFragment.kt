package com.example.playlistmaker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.settings.util.ActionType
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchTheme.isChecked = viewModel.getTheme()
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.execute(ActionType.Theme(checked))
        }
        binding.shareButton.setOnClickListener {
            viewModel.execute(ActionType.Share)
        }
        binding.supportButton.setOnClickListener {
            viewModel.execute(ActionType.Support)
        }
        binding.forwardButton.setOnClickListener {
            viewModel.execute(ActionType.Term)
        }
    }
}
