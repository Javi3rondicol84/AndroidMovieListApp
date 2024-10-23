package ar.edu.unicen.seminario.ddl.data

import ar.edu.unicen.seminario.ddl.model.Movie
import ar.edu.unicen.seminario.ddl.model.MovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieRepositoryDataSource: MovieRepositoryDataSource
) {

    suspend fun getMovieList(language: String): MovieResponseInfo {
        return  movieRepositoryDataSource.getMovieList(language)
    }

    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailResponseInfo {
        return movieRepositoryDataSource.getMovieDetails(movieId, language)
    }

    suspend fun getMovieListBySearch(search: String, language: String): MovieResponseInfo {
        return movieRepositoryDataSource.getMovieListBySearch(search, language)
    }

}