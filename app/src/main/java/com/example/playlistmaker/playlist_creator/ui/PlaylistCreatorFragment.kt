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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreatorBinding
import com.example.playlistmaker.main.ui.MainActivity.Companion.ALBOM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

open class PlaylistCreatorFragment : Fragment() {

    private val viewModel by viewModel<PlayListCreatorViewModel>()

    private var _binding: FragmentPlaylistCreatorBinding? = null
    private val binding get() = _binding!!
    private var nameInputText: String = ""
    private var descriptionTextWatcher: TextWatcher? = null
    private var nameTextWatcher: TextWatcher? = null
    private lateinit var filePath: File



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
        val backAlertDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.back_alert_title)
            .setMessage(R.string.back_alert_message)
            .setNegativeButton(R.string.negative_button) { _, _ ->
            }
            .setPositiveButton(getString(R.string.positive_button)) { _, _ ->
                findNavController().navigateUp()
            }

        binding.playListImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setDescription(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        descriptionTextWatcher?.let { binding.descriptionET.addTextChangedListener(it) }

        binding.createButton.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.getUri() != null)
                    viewModel.saveImage(
                        filePath,
                        viewModel.savePlaylist(filePath.toURI()),
                        viewModel.getUri()!!
                    ) else viewModel.savePlaylist(filePath.toURI())
                showToast(viewModel.getName())
                findNavController().navigateUp()
            }

        }

        binding.backButton.setOnClickListener {
            if (viewModel.checkInput()) backAlertDialog.show() else findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.checkInput()) backAlertDialog.show() else findNavController().navigateUp()
            }
        })


    }

    private fun showToast(playListName: String) {
        val message = getString(R.string.add_playlist_message).format(playListName)
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showPicture(pictureUri: String) {
        Glide.with(requireActivity())
            .load(pictureUri)
            .transform(
                CenterCrop(),
                RoundedCorners(requireActivity().resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art))
            )
            .into(binding.playListImage)
    }
}