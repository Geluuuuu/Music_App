package gateway.server.dto

class ContentDto (val id : Int,
                  val name : String,
                  val gen : String,
                  val year : Int,
                  val type : String,
                  val artists : List<ArtistData>) {
}