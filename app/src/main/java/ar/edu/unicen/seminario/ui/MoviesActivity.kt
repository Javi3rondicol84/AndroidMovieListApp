package ar.edu.unicen.seminario.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ActivityMoviesBinding
import ar.edu.unicen.seminario.ddl.data.ErrorType
import ar.edu.unicen.seminario.ui.moviedetail.MovieDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviesBinding
    private val viewModel by viewModels<MoviesViewModel>()
    private val searchQueryFlow = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToUi()
        subscribeToViewModel()

        // Aplicar debounce(delay) y buscar las películas solo después de que el usuario deja de escribir
        searchQueryFlow
            .debounce(500) // Esperar 500 ms después de que el usuario termine de escribir
            .onEach { query ->
                val language = getString(R.string.language)
                if (query.isNotBlank()) {

                    viewModel.getMoviesBySearch(query, language)

                } else {
                    // Si se borra todo en el text input mostrar la lista de peliculas
                    viewModel.getMovies(language)
                }
            }
            .launchIn(lifecycleScope)

        // Escuchar cambios en el EditText
        binding.moviesEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQueryFlow.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Antes de que el texto cambie
            }

            override fun afterTextChanged(s: Editable?) {
                // Después de que el texto cambie
            }
        })
    }

    private fun subscribeToUi() {
        val language = getString(R.string.language)
        viewModel.getMovies(language)
    }

    private fun subscribeToViewModel() {

        viewModel.loading.onEach { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

        viewModel.movies.onEach { movies ->
            binding.moviesList.adapter = MovieAdapter(
                movies = movies ?: emptyList(),
                onMoviesClick = { movie ->
                    val intent = Intent(this, MovieDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra("movieId", movie.id)
                    startActivity(intent)
                }
            )
        }.launchIn(lifecycleScope)

        viewModel.error.onEach { errorMessage ->

            if(errorMessage == ErrorType.NO_CONTENT) {
                Toast.makeText(this, R.string.error_no_content, Toast.LENGTH_LONG).show()
                binding.noContentSearch?.visibility = View.VISIBLE
            }
            else if(errorMessage == ErrorType.NO_INTERNET) {
                Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_LONG).show()
            }
            else if(errorMessage == ErrorType.ERROR_RESPONSE) {
                Toast.makeText(this, R.string.error_response, Toast.LENGTH_LONG).show()
            }
            else if(errorMessage == ErrorType.ERROR_UNEXPECTED) {
                Toast.makeText(this, R.string.error_unexpected, Toast.LENGTH_LONG).show()
            }

            viewModel.clearError()

        }.launchIn(lifecycleScope)

        viewModel.noContentSearch.onEach { noContent ->
            if(noContent) {
                binding.noContentSearch.visibility = View.VISIBLE
            }
            else {
                binding.noContentSearch.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

    }

}