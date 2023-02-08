package gateway.server.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.*
import java.util.UUID
import javax.persistence.*

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "artists", schema = "public")
class Artist (@Id
              @Column (name = "id")
              val id : UUID,
              @Column(name = "name")
              val name : String,
              @Column (name = "active")
              val active : Boolean,
              @ManyToMany(mappedBy = "artists")
              @JsonIgnoreProperties("artists")
              var contents: MutableList<Content> = mutableListOf()){
    @PreRemove
    private fun removeArtistsFromContents () {
        for (content : Content in contents) {
            content.artists.remove(this)
        }
    }
}