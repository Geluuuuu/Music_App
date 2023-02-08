package com.pos.playlist.repository

import com.pos.playlist.model.Playlist
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository : MongoRepository<Playlist, String> {
    fun findAllByUserId (userId : Int) : List<Playlist>
}