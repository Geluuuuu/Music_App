package gateway.server.dto

import gateway.server.entity.Artist
import gateway.server.utils.Genre
import gateway.server.utils.TypeMusic
import java.util.*

class ContentRequest (val name : String,
                      val gen : Genre,
                      val year : Int,
                      val type : TypeMusic,
                      val artists : List<UUID>) {
}