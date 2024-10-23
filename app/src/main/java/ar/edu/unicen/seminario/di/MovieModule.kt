package ar.edu.unicen.seminario.di

import ar.edu.unicen.seminario.ddl.data.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    @Provides
    @Singleton
    fun provideTokenKey(): String {
        return "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MjhhYTU5MWEwNmY5NzQ1ODQ0MTA3OGJhNDIxNTZlZCIsIm5iZiI6MTcyNzkxNTQzNC44NjgzNzQsInN1YiI6IjY2ZmRkNmJhYjE0NjI4MmY3Yjg0YzBjNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Wriv6OC_EtxoeKlbjHbslqnj20iDpmg4BYYVphEICso"
    }

    @Provides
    @Singleton
    fun providesMovieApi(
        retrofit: Retrofit
    ): MovieApi {
       return retrofit.create(MovieApi::class.java)
    }




}