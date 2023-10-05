package com.example.playlistmaker.playlist_editor

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.main.ui.MainActivity
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.playlist_creator.ui.PlaylistCreatorFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditorFragment : PlaylistCreatorFragment() {

    private lateinit var playList: PlayList
    override val viewModel by viewModel<PlaylistEditorViewModel>()
    private val hostViewModel by activityViewModel<MainActivityViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createButton.text = requireActivity().resources.getString(R.string.save)
        binding.headText.text = requireActivity().resources.getString(R.string.edit)
        playList = hostViewModel.getPlayList().value!!
        viewModel.observeSaveState().observe(viewLifecycleOwner) {
            rengerSave(it)
        }
        val filePath =
            File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                MainActivity.ALBOM
            )
        val file = File(filePath, "${playList.id}.jpg")
        binding.playListImage.setScaleType(ImageView.ScaleType.CENTER_CROP)
        binding.playListImage.setBackgroundResource(0)
        showPicture(file.toUri().toString())
        binding.nameET.setText(playList.name)
        binding.descriptionET.setText(playList.description)
    }

    fun rengerSave(playList: PlayList) {
        hostViewModel.setPlayList(playList)
        findNavController().navigateUp()
    }

    override fun savePlaylist() {
        viewModel.savePlaylist(filePath, playList)
    }

    override fun goBack() {
        findNavController().navigateUp()
    }
}