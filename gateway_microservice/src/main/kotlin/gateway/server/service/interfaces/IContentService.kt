package gateway.server.service.interfaces

import gateway.server.dto.*
import gateway.server.entity.Content

interface IContentService {
    fun add (request : ContentRequest)
    fun getContentById (id : Int) : ContentDto
    fun get (name : String, genre : String, year : Int,
             match: String, page : Int, itemsPerPage : Int) : ContentList
    fun delete (id : Int)
}