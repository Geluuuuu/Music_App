package com.pos.playlist.controller

import com.pos.playlist.dto.AddSongRequest
import com.pos.playlist.dto.DeleteSongRequest
import com.pos.playlist.dto.InsertRequest
import com.pos.playlist.dto.ListPlaylist
import com.pos.playlist.exception.UserNotMatch
import com.pos.playlist.model.Playlist
import com.pos.playlist.service.interfaces.IPlaylistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping ("/api/playlist")
class PlaylistController {
    @Autowired
    private lateinit var playlistService: IPlaylistService

    @RequestMapping (value = ["/"], method = [RequestMethod.PUT])
    fun insert (@RequestBody request: InsertRequest) : ResponseEntity <Void> {
        return try {
            playlistService.insert(request.title, request.userId)
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.GET])
    fun getAll () : ResponseEntity<ListPlaylist> {
        return try {
            val response = ListPlaylist(playlistService.getAll())
            ResponseEntity(response, HttpStatus.OK)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete (@PathVariable id : String, @RequestParam userId : Int) : ResponseEntity <Void> {
        return try {
            playlistService.delete(id, userId)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/song"], method = [RequestMethod.POST])
    fun addSong (@RequestBody request: AddSongRequest) : ResponseEntity<Void> {
        return try {
            playlistService.addSong(request.id, request.song, request.userId)
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/song"], method = [RequestMethod.DELETE])
    fun deleteSong (@RequestBody request: DeleteSongRequest) : ResponseEntity<Void> {
        return try {
            playlistService.deleteSong(request.id, request.songId, request.userId)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.GET])
    fun getByUser (@PathVariable id : Int) : ResponseEntity<ListPlaylist> {
        return try {
            val response : ListPlaylist = ListPlaylist(playlistService.getAllByUser(id))
            ResponseEntity (response, HttpStatus.OK)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}