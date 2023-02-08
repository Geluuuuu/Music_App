package gateway.server.service

import gateway.server.dto.ContentDto
import gateway.server.dto.ContentList
import gateway.server.dto.ContentRequest
import gateway.server.entity.Content
import gateway.server.exception.ArtistNotFoundException
import gateway.server.exception.ContentNotFoundException
import gateway.server.service.interfaces.IContentService
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
class ContentService : IContentService {
    @Value("\${links.api.content}")
    private lateinit var url : String

    override fun add(request: ContentRequest) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <ContentRequest> (request, headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange(url, HttpMethod.PUT, entity, Void::class.java)
        } catch (error : HttpClientErrorException.NotAcceptable) {
            throw ArtistNotFoundException("Not found")
        } catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun getContentById(id: Int): ContentDto {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<ContentDto> = restTemplate
                .exchange("${url}${id}", HttpMethod.GET, entity, ContentDto::class.java)
            return response.body!!
        } catch (error : HttpClientErrorException.NotFound) {
            throw ContentNotFoundException("Not found")
        }
        catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun get(
        name: String,
        genre: String,
        year: Int,
        match: String,
        page: Int,
        itemsPerPage: Int
    ): ContentList{
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        var apiUrl = UriComponentsBuilder.fromHttpUrl(url)
        val params: MutableMap<String, Any> = HashMap()

        if (name.compareTo("") == 0) {
            apiUrl = apiUrl.queryParam("name", "{name}")
            params["name"] = name
        }
        if (genre.compareTo("") == 0) {
            apiUrl = apiUrl.queryParam("genre", "{genre}")
            params["genre"] = genre
        }
        if (year != -1) {
            apiUrl = apiUrl.queryParam("year", "{year}")
            params["year"] = year
        }
        if (match.compareTo("") == 0) {
            apiUrl = apiUrl.queryParam("match", "{match}")
            params["match"] = match
        }
        if (page != -1) {
            apiUrl = apiUrl.queryParam("page", "{page}")
            params["page"] = page
        }
        if (itemsPerPage != -1) {
            apiUrl = apiUrl.queryParam("items_per_page", "{items_per_page}")
            params["items_per_page"] = itemsPerPage
        }

        val urlRequest : String = apiUrl.encode().toUriString()

        try {
            val response: ResponseEntity<ContentList> = restTemplate
                .exchange(urlRequest, HttpMethod.GET, entity, ContentList::class.java)
            return response.body!!
        } catch (exception : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }

    override fun delete(id: Int) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <Void> (headers)

        try {
            val response: ResponseEntity<Void> = restTemplate
                .exchange("${url}${id}", HttpMethod.DELETE, entity, Void::class.java)
        } catch (error : HttpClientErrorException.NotFound) {
            throw ContentNotFoundException("Not found")
        }
        catch (error : HttpServerErrorException.InternalServerError) {
            throw Exception()
        }
    }
}