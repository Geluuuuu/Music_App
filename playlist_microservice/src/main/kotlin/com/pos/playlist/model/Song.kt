package com.pos.playlist.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
class Song (val id : Int,
            val title : String,
            val href : String){
}