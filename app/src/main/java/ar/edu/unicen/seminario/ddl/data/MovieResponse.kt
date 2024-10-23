package ar.edu.unicen.seminario.ddl.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val movies: List<MovieDto>
)