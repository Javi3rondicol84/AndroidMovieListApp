package ar.edu.unicen.seminario.ddl.data
import ar.edu.unicen.seminario.ddl.model.MovieDetail
import ar.edu.unicen.seminario.ui.moviedetail.Genre
import com.google.gson.annotations.SerializedName


data class MovieDetailDto (
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val picture: String,
    @SerializedName("overview")
     val synopsis: String,
    @SerializedName("vote_average")
    val rating: Float,
    @SerializedName("genres")
    val genres: List<Genre>
){

    fun toMovieDetail(): MovieDetail {
        return MovieDetail(
            id = id,
            title = title,
            picture = picture,
            synopsis = synopsis,
            rating = rating,
            genres = genres.map { it.name }
        )
    }

}