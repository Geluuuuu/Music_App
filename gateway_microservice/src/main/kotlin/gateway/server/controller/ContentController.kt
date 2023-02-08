package gateway.server.controller

import gateway.server.dto.ContentDto
import gateway.server.dto.ContentList
import gateway.server.dto.ContentRequest
import gateway.server.dto.FilterRequest
import gateway.server.entity.Content
import gateway.server.exception.ArtistNotFoundException
import gateway.server.exception.ContentNotFoundException
import gateway.server.exception.RoleNotEnoughException
import gateway.server.service.interfaces.IContentService
import gateway.server.service.interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping ("api/gateway/content")
class ContentController {
    @Autowired
    private lateinit var contentService : IContentService

    @Autowired
    private lateinit var userService : IUserService

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.GET])
    fun getContentById (@RequestHeader token : String, @PathVariable id : Int) : ResponseEntity<ContentDto> {
        return try {
            userService.checkPermission(token, "user")
            val response : ContentDto = contentService.getContentById(id)
            ResponseEntity(response, HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : ContentNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.PUT])
    fun add (@RequestHeader token : String, @RequestBody content : ContentRequest) : ResponseEntity <Void> {
        return try {
            userService.checkPermission(token, "content_manager")
            contentService.add(content)
            ResponseEntity(HttpStatus.CREATED)
        } catch (exception : ArtistNotFoundException) {
            ResponseEntity (HttpStatus.NOT_ACCEPTABLE)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/"], method = [RequestMethod.GET])
    fun filter (@RequestParam name : Optional<String>, @RequestParam genre : Optional<String>,
                @RequestParam year : Optional<Int>, @RequestParam match : Optional<String>,
                @RequestParam items_per_page : Optional<Int>, @RequestParam page : Optional<Int>) :
            ResponseEntity<ContentList>{
        return try {
            val name1 : String = name.orElseGet { "" }
            val genre1 : String = genre.orElseGet{ "" }
            val year1 : Int = year.orElseGet{ -1 }
            val match1 : String = match.orElseGet{ "exact" }
            val page1 : Int = page.orElseGet{ -1 }
            var itemsPerPage : Int = items_per_page.orElseGet{ -1 }
            if (itemsPerPage < 0)
                itemsPerPage = -1

            ResponseEntity(contentService.get(name1, genre1, year1, match1, page1, itemsPerPage), HttpStatus.OK)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete (@PathVariable id : Int) : ResponseEntity<Void> {
        return try {
            contentService.delete(id)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : ContentNotFoundException) {
            ResponseEntity (HttpStatus.NOT_FOUND)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}