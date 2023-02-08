package gateway.server.service.interfaces

import gateway.server.dto.AddPlaylistRequest
import gateway.server.dto.AddSongRequest
import gateway.server.dto.DeleteSongRequest
import gateway.server.entity.Playlist

interface IPlaylistService {
    fun add (request : AddPlaylistRequest)
    fun getAll () : List<Playlist>
    fun delete (id : String, userId : Int)

    fun addSong (request : AddSongRequest)
    fun deleteSong (request : DeleteSongRequest)

    fun getByUser (id : Int) : List<Playlist>
}