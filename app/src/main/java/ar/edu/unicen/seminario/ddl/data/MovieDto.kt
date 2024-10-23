package ar.edu.unicen.seminario.ddl.data
import ar.edu.unicen.seminario.ddl.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto (
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val picture: String,
){

    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            picture = picture
        )
    }

}