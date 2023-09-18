package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.TracksEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TracksEntity)

    @Delete(entity = TracksEntity::class)
    suspend fun delete(track: TracksEntity)

    @Query("SELECT * FROM track_table ORDER BY updateTime DESC")
    suspend fun getFavoriteTracks(): List<TracksEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Long>
}
