package com.example.playlistmaker.player.ui.activity

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SmallPlaylistItemBinding
import com.example.playlistmaker.library.domain.model.PlayList
import java.io.File

class SmallPlayListViewHolder(
    private val binding: SmallPlaylistItemBinding,
    private val filePath: File
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PlayList) {
        binding.plName.text = item.name
        val addedText = itemView.resources.getQuantityString(
            R.plurals.tracksContOfList,
            item.trackCount.toInt(), item.trackCount
        )
        binding.plCount.text = addedText
        val file = File(filePath, "${item.id}.jpg")
        Glide.with(itemView)
            .load(file.toUri().toString())
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_art))
            )
            .into(binding.plImage)
    }
}