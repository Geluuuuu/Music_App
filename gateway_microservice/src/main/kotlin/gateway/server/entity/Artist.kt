package gateway.server.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

class Artist (val id : UUID,
              val name : String,
              val active : Boolean,
              val contents : List<Content>){
}