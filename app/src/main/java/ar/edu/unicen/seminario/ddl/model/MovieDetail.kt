package ar.edu.unicen.seminario.ddl.model

class MovieDetail (
    val id: Int,
    val title: String,
    val picture: String?,
    val synopsis: String,
    val rating: Float,
    val genres: List<String>
)