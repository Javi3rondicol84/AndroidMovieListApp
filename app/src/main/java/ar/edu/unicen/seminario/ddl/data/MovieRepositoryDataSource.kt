package ar.edu.unicen.seminario.ddl.data

import android.util.Log
import android.widget.Toast
import ar.edu.unicen.seminario.ddl.model.Movie
import ar.edu.unicen.seminario.ddl.model.MovieDetail
import ar.edu.unicen.seminario.ui.moviedetail.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryDataSource @Inject constructor(
    private val movieApi: MovieApi,
    private val tokenKey: String
){

    suspend fun getMovieList(language: String): MovieResponseInfo {
        val moviesResponseInfo = MovieResponseInfo(ErrorType.NO_ERROR, emptyList())
        return withContext(Dispatchers.IO) {
            try {

                val response = movieApi.getAllPopularMovies("Bearer $tokenKey", language)

                if(response.isSuccessful) {
                    val movies = response.body()?.movies?.map { it.toMovie() }

                    if(movies.isNullOrEmpty()) {
                        moviesResponseInfo.error = ErrorType.NO_CONTENT
                    }

                    moviesResponseInfo.movies = movies

                    return@withContext moviesResponseInfo
                }
                else {
                    moviesResponseInfo.error = ErrorType.ERROR_RESPONSE

                    return@withContext moviesResponseInfo
                }


            } catch (e: Exception) {
                when(e) {
                    is IOException -> {
                        moviesResponseInfo.error = ErrorType.NO_INTERNET
                    }
                    else -> {
                        moviesResponseInfo.error = ErrorType.ERROR_API
                    }
                }

                return@withContext moviesResponseInfo
            }
        }
    }

    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailResponseInfo {
        val movieDetailResponseInfo = MovieDetailResponseInfo(ErrorType.NO_ERROR, null)
        return withContext(Dispatchers.IO) {
            try {
                val response = movieApi.getMovieDetails(movieId, language, "Bearer $tokenKey")

                if(response.isSuccessful) {
                    val movie = response.body()

                    if(movie != null) {
                        movieDetailResponseInfo.movie = response.body()?.toMovieDetail()
                        return@withContext movieDetailResponseInfo
                    }
                    else {
                        movieDetailResponseInfo.error = ErrorType.NO_CONTENT
                        return@withContext movieDetailResponseInfo
                    }
                }
                else {
                    movieDetailResponseInfo.error = ErrorType.ERROR_RESPONSE
                    return@withContext movieDetailResponseInfo
                }

            } catch (e: Exception) {
                when(e) {
                    is IOException -> {
                        movieDetailResponseInfo.error = ErrorType.NO_INTERNET
                    }
                    else -> {
                        movieDetailResponseInfo.error = ErrorType.ERROR_API
                    }
                }

                return@withContext movieDetailResponseInfo
            }
        }
    }

    suspend fun getMovieListBySearch(search: String, language: String): MovieResponseInfo {
        val moviesResponseInfo = MovieResponseInfo(ErrorType.NO_ERROR, emptyList())
        return withContext(Dispatchers.IO) {
            try {

                val response = movieApi.getAllMoviesBySearch("Bearer $tokenKey", search, language)

                if(response.isSuccessful) {
                    val movies = response.body()?.movies?.map { it.toMovie() }

                    if(movies.isNullOrEmpty()) {
                        moviesResponseInfo.error = ErrorType.NO_CONTENT
                    }

                    moviesResponseInfo.movies = movies

                    return@withContext moviesResponseInfo
                }
                else {
                    moviesResponseInfo.error = ErrorType.ERROR_RESPONSE

                    return@withContext moviesResponseInfo
                }

            } catch (e: Exception) {
                when(e) {
                    is IOException -> {
                        moviesResponseInfo.error = ErrorType.NO_INTERNET
                    }
                    else -> {
                        moviesResponseInfo.error = ErrorType.ERROR_API
                    }
                }

                return@withContext moviesResponseInfo
            }
        }
    }

}