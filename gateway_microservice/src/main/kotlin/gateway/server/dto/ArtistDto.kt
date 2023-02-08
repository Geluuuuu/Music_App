package gateway.server.dto

import java.util.*

class ArtistDto (val id : UUID,
                 val name : String,
                 val active : Boolean,
                 val contents : List<ContentData>){
}