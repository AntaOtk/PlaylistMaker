package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.library.data.db.entity.PlayListEntity

@Dao
interface PlayListDao {

    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playList: PlayListEntity): Long

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlayLists(): List<PlayListEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playListId")
    suspend fun getPlayList(playListId: Long): List<PlayListEntity>

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("SELECT tracks FROM playlist_table WHERE id = :playListId")
    suspend fun getTrackList(playListId: Long): List<String>

    @Delete(entity = PlayListEntity::class)
    suspend fun delete(playlist: PlayListEntity)
}