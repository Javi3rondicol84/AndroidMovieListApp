package ar.edu.unicen.seminario.ddl.data

import ar.edu.unicen.seminario.ddl.model.Movie

class MovieResponseInfo (
    var error: ErrorType,
    var movies: List<Movie>?
){

}