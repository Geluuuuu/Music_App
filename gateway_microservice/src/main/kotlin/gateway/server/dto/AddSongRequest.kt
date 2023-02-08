package gateway.server.dto

import gateway.server.entity.Song

class AddSongRequest (val id : String, val song : Song, val userId : Int) {
}