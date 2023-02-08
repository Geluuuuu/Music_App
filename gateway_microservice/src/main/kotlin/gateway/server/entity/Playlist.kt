package gateway.server.entity

class Playlist (val id : String,
                val title : String,
                val songs : MutableList <Song>,
                val userId : Int){
}