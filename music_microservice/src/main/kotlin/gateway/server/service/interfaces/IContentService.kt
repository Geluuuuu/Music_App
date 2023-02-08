package gateway.server.service.interfaces

import gateway.server.dto.ContentDto
import gateway.server.dto.ContentRequest
import gateway.server.dto.FilterRequest
import gateway.server.entity.Content

interface IContentService {
    fun addContent (content : ContentRequest)
    fun delete (id : Int)
    fun getContentById (id : Int) : ContentDto
    fun filterPartialByName (name : String) : List <Content>
    fun filterCompleteByName (name : String) : List <Content>
    fun filterByGen (gen : String) : List <Content>
    fun filterByYear (year : Int) : List <Content>
    fun filterListByGen (list : List<Content>, gen : String) : List <Content>
    fun filterListByYear (list : List<Content>, year : Int) : List <Content>
    fun filter (request: FilterRequest) : List <Content>
    fun pagination (list: List<Content>, numberOfElementsPerPage: Int, pageNumber : Int) : List<ContentDto>
}