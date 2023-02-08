package gateway.server.service

import gateway.server.dto.ArtistDto
import gateway.server.dto.ArtistRequest
import gateway.server.dto.ContentData
import gateway.server.entity.Artist
import gateway.server.exception.ArtistNotFoundException
import gateway.server.repository.IArtistRepository
import gateway.server.service.interfaces.IArtistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException
import kotlin.streams.toList

@Service
class ArtistService : IArtistService {
    @Autowired
    private lateinit var artistRepository : IArtistRepository

    override fun addArtist(request: ArtistRequest) {
        artistRepository.save(Artist(UUID.randomUUID(), request.name, request.active))
    }

    override fun getArtistById(id: UUID): ArtistDto {
        if (!artistRepository.existsById(id))
            throw ArtistNotFoundException("Not found!")

        val artist = artistRepository.getReferenceById(id)
        return ArtistDto(artist.id, artist.name, artist.active,
            artist.contents
                .stream().map { content -> ContentData(content.id, content.name, content.gen, content.year, content.type) }
                .toList())
    }

    override fun getArtists(): List<ArtistDto> {
        return artistRepository.findAll()
            .stream().map { artist -> ArtistDto(artist.id, artist.name, artist.active
                , artist.contents.stream()
                    .map { content -> ContentData(content.id, content.name, content.gen, content.year, content.type) }
                    .toList()) }.toList()
    }

    override fun delete(id : UUID) {
        if (!artistRepository.existsById(id))
            throw ArtistNotFoundException("Not found!")
        artistRepository.deleteById(id)
    }
}