package ar.edu.unicen.seminario.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ListMovieItemBinding
import ar.edu.unicen.seminario.ddl.model.Movie
import com.bumptech.glide.Glide

class MovieAdapter (
    private var movies: List<Movie>,
    private val onMoviesClick:(Movie) -> Unit
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ListMovieItemBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

   inner class MovieViewHolder(
        private val binding: ListMovieItemBinding
    ): RecyclerView.ViewHolder(binding.root){

         fun bind(movie: Movie) {
             //colocar titulo
            binding.movieTitle.text = movie.title

            //añadir imagen
            if (movie.picture != null) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w500/" + movie.picture) //base de imagen + path de api
                    .placeholder(R.drawable.movie_main_image_app)
                    .error(R.drawable.movie_main_image_app)
                    .into(binding.movieImage)
            }
            else {
                Glide.with(itemView.context)
                    .load(R.drawable.movie_main_image_app) //imagen default cuando no se haya ninguna otra
                    .placeholder(R.drawable.movie_main_image_app)
                    .into(binding.movieImage)
            }

            //seleccionar una pelicula
            binding.root.setOnClickListener() {
                onMoviesClick(movie)
            }
        }

    }

}