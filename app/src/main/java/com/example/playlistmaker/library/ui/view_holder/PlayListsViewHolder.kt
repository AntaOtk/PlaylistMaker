package com.example.playlistmaker.library.ui.view_holder


import android.annotation.SuppressLint
import androidx.core.content.ContextCompat.getString
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.domain.model.PlayList
import java.io.File

class PlayListsViewHolder(private val binding: PlaylistItemBinding, private val filePath: File) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: PlayList) {
        binding.plName.text = item.name
        val addedText = getString(itemView.context, addText(item.trackCount))
        binding.plCount.text = "${item.trackCount} ${addedText}"
        val file = File(filePath, "${item.id}.jpg")
        Glide.with(itemView)
            .load(file.toUri().toString())
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art)))
            .into(binding.plImage)
    }

    private fun addText(trackCount: Long): Int {
        val dec = trackCount.toInt() % 100
        val ones = dec % 10
        val tens = dec / 10
        return if (ones == 1 && tens != 1) {
            R.string.one_track
        } else if (ones in 2..4 && tens != 1) {
            R.string.two_tracks

        } else {
            R.string.tracks
        }
    }
}
