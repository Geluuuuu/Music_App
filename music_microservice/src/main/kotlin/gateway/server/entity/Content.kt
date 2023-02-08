package gateway.server.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.*
import javax.persistence.*

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table (name = "content", schema = "public")
class Content (@Id
               @GeneratedValue (strategy = GenerationType.IDENTITY)
               @Column (name = "id")
               val id : Int,
               @Column (name = "name")
               val name : String,
               @Column (name = "gen")
               val gen : String,
               @Column (name = "year")
               val year : Int,
               @Column (name = "type")
               val type : String,
               @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
               @JoinTable(name = "content_artist",
                    joinColumns = [JoinColumn(name = "content_id", referencedColumnName = "id")],
                    inverseJoinColumns = [JoinColumn(name = "artist_id", referencedColumnName = "id")])
               @JsonIgnoreProperties("contents")
               var artists: MutableList<Artist> = mutableListOf()) {
    fun addArtist (artist : Artist) {
        this.artists.add (artist)
        artist.contents.add (this)
    }

    @PreRemove
    private fun removeContentsFromArtists () {
        for (artist : Artist in artists) {
            artist.contents.remove(this)
        }
    }
}