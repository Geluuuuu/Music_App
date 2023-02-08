package gateway.server.dto

import com.fasterxml.jackson.annotation.JsonProperty
import gateway.server.entity.User

class UserList (@JsonProperty ("users")
                val users : List<User>) {
}