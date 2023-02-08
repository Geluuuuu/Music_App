package gateway.server.controller

import gateway.server.dto.ArtistDto
import gateway.server.dto.ArtistList
import gateway.server.dto.ArtistRequest
import gateway.server.entity.Artist
import gateway.server.exception.ArtistNotFoundException
import gateway.server.service.interfaces.IArtistService
import net.minidev.json.JSONValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.sql.SQLException
import java.util.UUID

@RestController
@RequestMapping ("api/songcollection/artists")
class ArtistController {
    @Autowired
    private lateinit var artistService : IArtistService

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.GET])
    fun getArtistById (@PathVariable id : UUID) : ResponseEntity<ArtistDto>{
        return try {
            val response : ArtistDto = artistService.getArtistById(id)
            ResponseEntity(response, HttpStatus.OK)
        } catch (exception : ArtistNotFoundException) {
          ResponseEntity (HttpStatus.NOT_FOUND)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.PUT])
    fun add (@RequestBody request : ArtistRequest) : ResponseEntity<Void> {
        return try {
            artistService.addArtist(request)
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.GET])
    fun get () : ResponseEntity<ArtistList> {
        return try {
            val response : ArtistList = ArtistList(artistService.getArtists())
            ResponseEntity (response, HttpStatus.OK)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.DELETE])
    fun delete (@PathVariable id : UUID) : ResponseEntity<Void> {
        return try {
            artistService.delete(id)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : ArtistNotFoundException) {
            ResponseEntity (HttpStatus.NOT_FOUND)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}