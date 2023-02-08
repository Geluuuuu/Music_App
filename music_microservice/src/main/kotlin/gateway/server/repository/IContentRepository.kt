package gateway.server.repository

import gateway.server.entity.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IContentRepository : JpaRepository <Content, Int>{
    fun findByNameContaining (name : String) : List <Content>
    fun getAllByName (name : String) : List <Content>
    fun getAllByYear (year : Int) : List<Content>
    fun getAllByGen (gen : String) : List<Content>
}