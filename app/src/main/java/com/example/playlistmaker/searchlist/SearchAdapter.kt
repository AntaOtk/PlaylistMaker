package com.example.playlistmaker.searchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class SearchAdapter(private val data: List<Track>): RecyclerView.Adapter<SearchViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchViewHolder(view)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}