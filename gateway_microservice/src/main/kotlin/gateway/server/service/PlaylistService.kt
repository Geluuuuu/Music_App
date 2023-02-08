package gateway.server.service

import gateway.server.dto.*
import gateway.server.entity.Playlist
import gateway.server.service.interfaces.IPlaylistService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class PlaylistService : IPlaylistService{
    @Value("\${links.api.playlist}")
    private lateinit var url : String

    override fun add(request: AddPlaylistRequest) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <AddPlaylistRequest> (request, headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange(url, HttpMethod.PUT, entity, Void::class.java)
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun getAll(): List<Playlist> {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<ListPlaylist> = restTemplate
                .exchange(url, HttpMethod.GET, entity, ListPlaylist::class.java)
            return response.body!!.playlists
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun delete(id: String, userId: Int) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        val apiUrl = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("userId", "{userId}")
            .encode()
            .toUriString()
        val params: MutableMap<String, Int> = HashMap()
        params["userId"] = userId

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange("${apiUrl}${id}", HttpMethod.DELETE, entity, Void::class.java, params)
        } catch (error : HttpClientErrorException.Forbidden) {
            throw UserNotMatch ("Not match")
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun addSong(request: AddSongRequest) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <AddSongRequest> (request, headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange("${url}song", HttpMethod.POST, entity, Void::class.java)
        } catch (error : HttpClientErrorException.Forbidden) {
            throw UserNotMatch ("Not match")
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun deleteSong(request: DeleteSongRequest) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <DeleteSongRequest> (request, headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange("${url}song", HttpMethod.DELETE, entity, Void::class.java)
        } catch (error : HttpClientErrorException.Forbidden) {
            throw UserNotMatch ("Not match")
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun getByUser(id: Int): List<Playlist> {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<ListPlaylist> = restTemplate
                .exchange("${url}${id}", HttpMethod.GET, entity, ListPlaylist::class.java)
            return response.body!!.playlists
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }
}