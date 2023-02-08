package com.pos.playlist.service.interfaces

import com.pos.playlist.model.Playlist
import com.pos.playlist.model.Song

interface IPlaylistService {
    fun insert (title : String, userId : Int)
    fun delete (playlistId : String, userId : Int)

    fun addSong (playlistId : String, song : Song, userId : Int)
    fun deleteSong (playlistId: String, songId : Int, userId : Int)

    fun getAll () : List <Playlist>
    fun getAllByUser (userId : Int) : List<Playlist>
}