package gateway.server.entity

import com.fasterxml.jackson.annotation.JsonProperty

class User (@JsonProperty("id")
            val id : Int,
            @JsonProperty("username")
            val username : String,
            @JsonProperty("password")
            val password : String,
            @JsonProperty("roles")
            val roles : List<String>){
}