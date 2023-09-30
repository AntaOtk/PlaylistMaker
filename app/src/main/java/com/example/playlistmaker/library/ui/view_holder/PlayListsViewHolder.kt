package com.example.playlistmaker.library.ui.view_holder


import android.annotation.SuppressLint
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
        val addedText = itemView.resources.getQuantityString(
            R.plurals.tracksContOfList,
            item.trackCount.toInt(), item.trackCount.toInt()
        )
        binding.plCount.text = addedText
        val file = File(filePath, "${item.id}.jpg")
        Glide.with(itemView)
            .load(file.toUri().toString())
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art))
            )
            .into(binding.plImage)
    }

}
