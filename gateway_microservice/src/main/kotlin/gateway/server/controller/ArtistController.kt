package gateway.server.controller

import gateway.server.dto.ArtistList
import gateway.server.dto.ArtistRequest
import gateway.server.entity.Artist
import gateway.server.exception.RoleNotEnoughException
import gateway.server.service.interfaces.IArtistService
import gateway.server.service.interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping ("api/gateway/artists")
class ArtistController {
    @Autowired
    private lateinit var artistService : IArtistService

    @Autowired
    private lateinit var userService : IUserService

    @RequestMapping (value = ["/"], method = [RequestMethod.POST])
    fun add (@RequestHeader token : String, @RequestBody request : ArtistRequest) : ResponseEntity<Void> {
        return try {
            userService.checkPermission(token, "content_manager")
            artistService.add(request.name, request.active)
            ResponseEntity(HttpStatus.CREATED)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity(HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.GET])
    fun get (@RequestHeader token : String) : ResponseEntity<ArtistList> {
        return try {
            userService.checkPermission(token, "content_manager")
            val response : ArtistList = artistService.get()
            ResponseEntity (response, HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}