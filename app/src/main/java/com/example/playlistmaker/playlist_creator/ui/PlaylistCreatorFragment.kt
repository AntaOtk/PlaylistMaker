package com.example.playlistmaker.playlist_creator.ui

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import com.example.playlistmaker.main.ui.MainActivity.Companion.ALBOM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class PlaylistCreatorFragment : Fragment() {

    open val viewModel by viewModel<PlayListCreatorViewModel>()

    private var _binding: FragmentPlaylistCreatorBinding? = null
    val binding get() = _binding!!
    private var nameInputText: String = ""
    private var descriptionTextWatcher: TextWatcher? = null
    private var nameTextWatcher: TextWatcher? = null
    lateinit var filePath: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBOM)
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    showPicture(uri.toString())
                    viewModel.setUri(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderSave(it)
        }
        viewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            renderSave(it)
        }
        binding.playListImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameInputText = s?.toString() ?: ""
                viewModel.setName(nameInputText)
                binding.createButton.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        nameTextWatcher?.let { binding.nameET.addTextChangedListener(it) }
        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        descriptionTextWatcher?.let { binding.descriptionET.addTextChangedListener(it) }

        binding.createButton.setOnClickListener {
            savePlaylist()
        }

        binding.backButton.setOnClickListener {
            goBack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun savePlaylist() {
        viewModel.savePlaylist(filePath)
    }

    open fun goBack() {
        if (viewModel.checkInput()) showDialog() else findNavController().navigateUp()
    }


    private fun showDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setTitle(R.string.back_alert_title)
            .setMessage(R.string.back_alert_message)
            .setNegativeButton(R.string.negative_button) { _, _ ->
            }
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

    private fun renderSave(playListName: String) {
        findNavController().navigateUp()
        val message = getString(R.string.add_playlist_message).format(playListName)
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun showPicture(pictureUri: String) {
        Glide.with(requireActivity())
            .load(pictureUri)
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art))
            )
            .into(binding.playListImage)
    }
}