package gateway.server.service.interfaces

import gateway.server.dto.ArtistDto
import gateway.server.dto.ArtistRequest
import gateway.server.entity.Artist
import java.util.UUID

interface IArtistService {
    fun addArtist (request : ArtistRequest)
    fun getArtistById (id : UUID) : ArtistDto
    fun getArtists () : List<ArtistDto>
    fun delete (id : UUID)
}