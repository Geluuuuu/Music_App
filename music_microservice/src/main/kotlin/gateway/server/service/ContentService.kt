package gateway.server.service

import gateway.server.dto.*
import gateway.server.entity.Artist
import gateway.server.entity.Content
import gateway.server.exception.ArtistNotFoundException
import gateway.server.exception.ContentNotFoundException
import gateway.server.repository.IArtistRepository
import gateway.server.repository.IContentRepository
import gateway.server.service.interfaces.IContentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class ContentService : IContentService {
    @Autowired
    private lateinit var contentRepository : IContentRepository

    @Autowired
    private lateinit var artistRepository : IArtistRepository

    override fun addContent(content: ContentRequest) {
        val newContent: Content = Content(0, content.name, content.gen.name, content.year, content.type.name)

        for (id: UUID in content.artists) {
            if (!artistRepository.existsById(id))
                throw ArtistNotFoundException ("Not found!")

            val artist: Artist = artistRepository.getReferenceById(id)
            newContent.addArtist(artist)
        }

        contentRepository.save(newContent)
    }

    override fun delete(id: Int) {
        if (!contentRepository.existsById(id))
            throw ContentNotFoundException ("Not found")

        contentRepository.deleteById(id)
    }

    override fun getContentById(id: Int): ContentDto {
        if (!contentRepository.existsById(id))
            throw ContentNotFoundException ("Not found")

        val content : Content = contentRepository.getReferenceById(id)
        return ContentDto(content.id, content.name, content.gen, content.year, content.type,
            content.artists.stream().map { artist -> ArtistData(artist.id, artist.name, artist.active) }.toList())
    }

    override fun filterPartialByName(name: String): List<Content> {
        return contentRepository.findByNameContaining(name)
    }

    override fun filterCompleteByName(name: String): List<Content> {
        return contentRepository.getAllByName(name)
    }

    override fun filterByGen(gen: String): List<Content> {
        return contentRepository.getAllByGen(gen)
    }

    override fun filterByYear(year: Int): List<Content> {
        return contentRepository.getAllByYear(year)
    }

    override fun filterListByGen(list: List<Content>, gen: String): List<Content> {
        return list.stream().filter{ element -> element.gen == gen }.toList()
    }

    override fun filterListByYear(list: List<Content>, year: Int): List<Content> {
        return list.stream().filter{ element -> element.year == year }.toList()
    }

    override fun filter(request: FilterRequest): List<Content> {
        var filtered = false
        var listContent : List <Content> = listOf()

        val name = request.name
        val year = request.year
        val genre = request.genre
        val match = request.match

        //by name
        if (name.compareTo("") != 0){
            if (match.compareTo("exact") == 0)
                listContent = filterCompleteByName(name)
            else
                listContent = filterPartialByName(name)
            filtered = true
        }

        //by year
        if (year >= 0) {
            if (filtered) {
                listContent = filterListByYear(listContent, year)
            }
            else {
                listContent = filterByYear(year)
                filtered = true
            }
        }

        //by genre
        if (genre.compareTo("") != 0) {
            if (filtered) {
                listContent = filterListByGen(listContent, genre)
            }
            else {
                listContent = filterByGen(genre)
                filtered = true
            }
        }

        if (!filtered)
            listContent = contentRepository.findAll()

        return listContent
    }

    override fun pagination(list: List<Content>, numberOfElementsPerPage: Int, pageNumber : Int): List<ContentDto> {
        val length : Int = list.size
        var numberPages : Int = length / numberOfElementsPerPage
        if (length % numberOfElementsPerPage != 0)
            numberPages++

        var page = pageNumber
        if (page < 1)
            page = 1

        val contentList = if (page >= numberPages)
            list.subList((page - 1) * numberOfElementsPerPage, length)
        else
            list.subList((page - 1) * numberOfElementsPerPage, page * numberOfElementsPerPage)

        return contentList
            .stream().map { content -> ContentDto(content.id, content.name, content.gen, content.year, content.type,
            content.artists.stream().map { artist -> ArtistData(artist.id, artist.name, artist.active) }.toList())}
            .toList()
    }
}