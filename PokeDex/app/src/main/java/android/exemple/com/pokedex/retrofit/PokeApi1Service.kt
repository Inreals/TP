package android.exemple.com.pokedex.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi1Service {

    @GET("generation/")
    fun generationCount(): Call<GenCount>

    @GET("generation/{number}")
    fun listePokemonGenN(@Path("number") number: Int?): Call<PokemonSpecyList>

    @GET("pokemon/{number}")
    fun getPokemonDescription(@Path("number")number: Int?): Call<PokemonInformation>

    @GET("pokemon-species/{number}")
    fun getPokemonDescriptionSuite(@Path("number")number: Int?): Call<PokemonInformationSuite>

    @GET("pokemon/{number}")
    fun getEvolution(@Path("number")number: Int?): Call<List<PokemonEvolutions>>

    companion object {
        const val ENDPOINT = "https://pokeapi.co/api/v2/"
        const val ENDPOINT2 = "https://pokeapi.glitch.me/v1/"
    }
}