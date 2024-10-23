package ar.edu.unicen.seminario.ddl.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getAllPopularMovies(
        @Header("Authorization") token: String,
        @Query("language") language: String
    ): retrofit2.Response<MovieResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: Int,
        @Query("language") language: String,
        @Header("Authorization") token: String
    ): retrofit2.Response<MovieDetailDto?>

    @GET("search/movie")
    suspend fun getAllMoviesBySearch(
        @Header("Authorization") token: String,
        @Query("query") search: String,
        @Query("language") language: String
    ): retrofit2.Response<MovieResponse>

}