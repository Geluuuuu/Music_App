package com.pos.playlist.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Playlist (@Id
                val id : String,
                val title : String,
                val songs : MutableList <Song>,
                val userId : Int){
}