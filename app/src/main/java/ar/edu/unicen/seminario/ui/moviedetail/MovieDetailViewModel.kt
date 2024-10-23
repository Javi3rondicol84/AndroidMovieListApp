package ar.edu.unicen.seminario.ui.moviedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.ddl.data.ErrorType
import ar.edu.unicen.seminario.ddl.data.MovieRepository
import ar.edu.unicen.seminario.ddl.model.Movie
import ar.edu.unicen.seminario.ddl.model.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.spi.internal.shaded.kotlinx.metadata.internal.metadata.serialization.MutableTable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail = _movieDetail.asStateFlow()

    private val _error = MutableStateFlow<ErrorType>(ErrorType.NO_ERROR)
    val error = _error.asStateFlow()

    private val _returnButton = MutableStateFlow(false)
    val returnButton = _returnButton.asStateFlow()

    fun getMovieDetail(movieId: Int, language: String) {
        viewModelScope.launch {
            _loading.value = true
            _movieDetail.value = null

            val movieDetailResponseInfo = movieRepository.getMovieDetails(movieId, language)

            _loading.value = false
            _movieDetail.value = movieDetailResponseInfo.movie // Manejo de null
            _error.value = movieDetailResponseInfo.error
        }

    }

    fun clearError() {
        _error.value = ErrorType.NO_ERROR
    }

}