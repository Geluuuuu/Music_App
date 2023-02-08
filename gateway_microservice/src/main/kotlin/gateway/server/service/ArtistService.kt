package gateway.server.service

import gateway.server.dto.ArtistDto
import gateway.server.dto.ArtistList
import gateway.server.dto.ArtistRequest
import gateway.server.entity.Artist
import gateway.server.exception.ArtistNotFoundException
import gateway.server.service.interfaces.IArtistService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class ArtistService : IArtistService{
    @Value("\${links.api.artist}")
    private lateinit var url : String

    override fun add(name: String, active: Boolean) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <ArtistRequest> (ArtistRequest (name, active), headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange(url, HttpMethod.PUT, entity, Void::class.java)
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun get(): ArtistList {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<ArtistList> = restTemplate
                .exchange(url, HttpMethod.GET, entity, ArtistList::class.java)
            return response.body!!
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun getArtistById(id: UUID): ArtistDto {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<ArtistDto> = restTemplate
                .exchange("${url}${id}", HttpMethod.GET, entity, ArtistDto::class.java)
            return response.body!!
        } catch (error : HttpClientErrorException.NotFound) {
            throw ArtistNotFoundException("Not found")
        }
        catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun delete(id: UUID) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange("${url}${id}", HttpMethod.DELETE, entity, Void::class.java)
        } catch (error : HttpClientErrorException.NotFound) {
            throw ArtistNotFoundException("Not found")
        }
        catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }
}