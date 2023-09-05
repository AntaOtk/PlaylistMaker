package com.example.playlistmaker.library.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.library.ui.view_holder.FavoriteViewHolder

class FavoriteAdapter(private val data: List<Track>, private val clickListener: (Track) -> Unit) :
RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(data[position])
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }
}
