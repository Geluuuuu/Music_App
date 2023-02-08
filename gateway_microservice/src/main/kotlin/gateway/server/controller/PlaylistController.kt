package gateway.server.controller

import gateway.server.dto.*
import gateway.server.exception.RoleNotEnoughException
import gateway.server.service.interfaces.IPlaylistService
import gateway.server.service.interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping ("api/gateway/playlist")
class PlaylistController {
    @Autowired
    private lateinit var playlistService: IPlaylistService

    @Autowired
    private lateinit var userService: IUserService

    @RequestMapping (value = ["/"], method = [RequestMethod.PUT])
    fun insert (@RequestHeader token : String, @RequestParam title : String) : ResponseEntity<Void> {
        return try {
            val id : Int = userService.checkPermission(token, "user")
            playlistService.add(AddPlaylistRequest(title, id))
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.GET])
    fun getAll (@RequestHeader token : String) : ResponseEntity<ListPlaylist> {
        return try {
            userService.checkPermission(token, "user")
            val response = ListPlaylist(playlistService.getAll())
            ResponseEntity(response, HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete (@RequestHeader token : String, @PathVariable id : String) : ResponseEntity <Void> {
        return try {
            val userId : Int = userService.checkPermission(token, "user")
            playlistService.delete(id, userId)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/song"], method = [RequestMethod.POST])
    fun addSong (@RequestHeader token : String, @RequestBody request: AddSongRequestGateway) : ResponseEntity<Void> {
        return try {
            val userId : Int = userService.checkPermission(token, "user")
            playlistService.addSong(AddSongRequest(request.id, request.song, userId))
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/song"], method = [RequestMethod.DELETE])
    fun deleteSong (@RequestHeader token : String, @RequestBody request: DeleteSongRequestGateway) : ResponseEntity<Void> {
        return try {
            val userId : Int = userService.checkPermission(token, "user")
            playlistService.deleteSong(DeleteSongRequest (request.id, request.songId, userId))
            ResponseEntity (HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : UserNotMatch) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.GET])
    fun getByUser (@RequestHeader token : String) : ResponseEntity<ListPlaylist> {
        return try {
            val userId : Int = userService.checkPermission(token, "user")
            val response : ListPlaylist = ListPlaylist(playlistService.getByUser(userId))
            ResponseEntity (response, HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}