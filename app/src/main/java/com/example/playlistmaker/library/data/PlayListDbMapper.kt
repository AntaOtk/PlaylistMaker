package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.entity.PlayListEntity
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track

class PlayListDbMapper() {

    fun map(playList: PlayList, tracks: String): PlayListEntity {
        return PlayListEntity(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            tracks
        )
    }

    fun map(playList: PlayListEntity, tracks: MutableList<Track>): PlayList {
        return PlayList(
            playList.id,
            playList.name,
            playList.description,
            playList.imageUrl,
            playList.trackCount,
            tracks
        )
    }
}
