package com.example.playlistmaker.searchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class SearchAdapter(private val data: List<Track>, val clickListener: TrackListener) :
    RecyclerView.Adapter<SearchViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {clickListener.onTrackClick(data[position]) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun interface TrackListener {
        fun onTrackClick(track: Track)
    }
}