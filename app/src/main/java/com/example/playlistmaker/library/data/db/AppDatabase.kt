package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.entity.PlayListEntity
import com.example.playlistmaker.library.data.db.entity.TracksEntity

@Database(version = 1, entities = [TracksEntity::class, PlayListEntity::class])
abstract class AppDatabase : RoomDatabase() {


    abstract fun trackDao(): TrackDao

    abstract fun playListDao(): PlayListDao
}
