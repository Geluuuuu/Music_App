package com.pos.playlist.service

import com.pos.playlist.exception.UserNotMatch
import com.pos.playlist.model.Playlist
import com.pos.playlist.model.Song
import com.pos.playlist.repository.PlaylistRepository
import com.pos.playlist.service.interfaces.IPlaylistService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlaylistService : IPlaylistService {
    @Autowired
    private lateinit var playlistRepository: PlaylistRepository

    override fun insert(title: String, userId: Int) {
        playlistRepository.save(Playlist(ObjectId().toString(),title, mutableListOf(), userId))
    }

    override fun delete(playlistId: String, userId : Int) {
        val playlist : Playlist = playlistRepository.findById(playlistId).get()
        if (playlist.userId != userId)
            throw UserNotMatch ("Not match")

        playlistRepository.delete(playlist)
    }

    override fun addSong(playlistId: String, song: Song, userId : Int) {
        val playlist : Playlist = playlistRepository.findById(playlistId).get()

        if (playlist.userId != userId)
            throw UserNotMatch ("Not match")

        playlist.songs.add(song)

        playlistRepository.save(playlist)
    }

    override fun deleteSong (playlistId: String, songId : Int, userId : Int) {
        val playlist : Playlist = playlistRepository.findById(playlistId).get()

        if (playlist.userId != userId)
            throw UserNotMatch ("Not match")

        playlist.songs.removeIf { element -> element.id == songId }

        playlistRepository.save(playlist)
    }

    override fun getAll(): List<Playlist> {
        return playlistRepository.findAll().toList()
    }

    override fun getAllByUser(userId: Int) : List<Playlist> {
        return playlistRepository.findAllByUserId(userId)
    }
}