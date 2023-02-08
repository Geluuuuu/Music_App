package gateway.server.repository

import gateway.server.entity.Artist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IArtistRepository : JpaRepository <Artist, UUID>{
}