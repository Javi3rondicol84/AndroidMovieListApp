package ar.edu.unicen.seminario.ui.moviedetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.MovieDetailBinding
import ar.edu.unicen.seminario.ddl.data.ErrorType
import ar.edu.unicen.seminario.ui.MovieAdapter
import ar.edu.unicen.seminario.ui.MoviesActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding : MovieDetailBinding
    private val viewModel by viewModels<MovieDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MovieDetailActivity, MoviesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        })

        binding = MovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToUi()
        subscribeToViewModel()
    }

    private fun subscribeToUi() {

        val movieId = intent.getIntExtra("movieId", 0)
        val language = getString(R.string.language)

        viewModel.getMovieDetail(movieId, language)
    }

    private fun subscribeToViewModel() {

        viewModel.loading.onEach { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }.launchIn(lifecycleScope)

        viewModel.movieDetail.onEach { movieDetail ->
            if (movieDetail != null) {
                val genres = concatenateGenres(movieDetail.genres)

                binding.movieTitle.text = movieDetail.title
                binding.movieSynopsis.text = movieDetail.synopsis
                binding.movieRating.text = movieDetail.rating.toString()
                binding.movieGenres.text = genres

                if (!movieDetail.picture.isNullOrEmpty()) {
                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w500/${movieDetail.picture}")
                        .into(binding.movieImage)
                }

            } else {
                binding.movieTitle.text = ""
                binding.movieSynopsis.text = ""
                binding.movieRating.text = ""
                binding.movieGenres.text = ""
                //binding.movieImage.setImageResource(R.drawable.placeholder) // Set a placeholder image if no picture

            }

            binding.returnButton.setOnClickListener {
                val intent = Intent(this, MoviesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }

        }.launchIn(lifecycleScope)

        viewModel.error.onEach { errorMessage ->
            if(errorMessage == ErrorType.NO_CONTENT) {
                Toast.makeText(this, R.string.error_no_content, Toast.LENGTH_LONG).show()
            }
            else if(errorMessage == ErrorType.NO_INTERNET) {
                Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_LONG).show()
            }
            else if(errorMessage == ErrorType.ERROR_RESPONSE) {
                Toast.makeText(this, R.string.error_response, Toast.LENGTH_LONG).show()
            }
            else if(errorMessage == ErrorType.ERROR_API) {
                Toast.makeText(this, R.string.error_api, Toast.LENGTH_LONG).show()
            }
            viewModel.clearError()
        }.launchIn(lifecycleScope)
    }

    private fun concatenateGenres(genresList: List<String>): String {
        var genres: String = ""

        for(genre in genresList) {
            genres += "  $genre \n"
        }

        return genres
    }

}