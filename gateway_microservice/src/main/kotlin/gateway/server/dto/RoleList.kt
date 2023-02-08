package gateway.server.dto

import com.fasterxml.jackson.annotation.JsonProperty

class RoleList (@JsonProperty ("roles")
                val roles : List<String>)