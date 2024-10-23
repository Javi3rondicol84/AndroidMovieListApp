package ar.edu.unicen.seminario.ddl.data

import ar.edu.unicen.seminario.ddl.model.MovieDetail

class MovieDetailResponseInfo (
    var error: ErrorType,
    var movie: MovieDetail?
){
}