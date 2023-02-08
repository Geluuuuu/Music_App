package com.pos.playlist.dto

import com.pos.playlist.model.Song

class AddSongRequest (val id : String, val song : Song, val userId : Int) {
}