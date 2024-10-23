package ar.edu.unicen.seminario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.ddl.data.ErrorType
import ar.edu.unicen.seminario.ddl.data.MovieRepository
import ar.edu.unicen.seminario.ddl.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel(){

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>?>(null)
    val movies = _movies.asStateFlow()

    private val _error = MutableStateFlow<ErrorType>(ErrorType.NO_ERROR)
    val error = _error.asStateFlow()

    private val _noContentSearch = MutableStateFlow(false)
    val noContentSearch = _noContentSearch.asStateFlow()

    fun getMovies(language: String) {
        viewModelScope.launch {
            _loading.value = true
            _movies.value = emptyList() // Limpiar lista antes de la carga
            _noContentSearch.value = false

            val moviesResponseInfo = movieRepository.getMovieList(language)

            _loading.value = false
            _movies.value = moviesResponseInfo.movies ?: emptyList()
            _error.value = moviesResponseInfo.error

        }
    }

    fun getMoviesBySearch(search: String, language: String) {
        viewModelScope.launch {
            _loading.value = true
            _movies.value = emptyList() // Limpiar lista antes de la carga
            _noContentSearch.value = false

            val moviesResponseInfo = movieRepository.getMovieListBySearch(search, language)

            _loading.value = false
            _movies.value = moviesResponseInfo.movies ?: emptyList()
            _error.value = moviesResponseInfo.error
            _noContentSearch.value = moviesResponseInfo.error == ErrorType.NO_CONTENT
        }
    }

    fun clearError() {
        _error.value = ErrorType.NO_ERROR
    }

}