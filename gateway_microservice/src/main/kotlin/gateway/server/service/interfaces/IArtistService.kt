package gateway.server.service.interfaces

import gateway.server.dto.ArtistDto
import gateway.server.dto.ArtistList
import java.util.UUID

interface IArtistService {
    fun add (name : String, active : Boolean)
    fun get () : ArtistList
    fun getArtistById (id : UUID) : ArtistDto
    fun delete (id : UUID)
}