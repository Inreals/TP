package android.exemple.com.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PokemonDatabaseDao {

    @Insert
    fun insert(pokemon: Pokemon)

    @Insert
    fun insert(pokemon: List<Pokemon>)

    @Query("SELECT * FROM pokemon_list WHERE generationId=:generationNumber")
    fun getPokemonGen(generationNumber: Int) : List<Pokemon>

    @Query("UPDATE pokemon_list SET nom=:nom,description=:description,taille=:taille,poids=:poids,types=:types WHERE pokemonId=:pokemonId")
    fun updatePokemonInfo(pokemonId: Int,nom:String,description: String,taille: String,poids: String,types: String)

    @Query("SELECT * FROM pokemon_list WHERE pokemonId=:pokemonId")
    fun pokemonInDb(pokemonId: Int) : Pokemon

    @Query("SELECT pokemonId FROM pokemon_list WHERE name IN (:pokemonName)")
    fun getPokemonId(pokemonName: List<String>) : List<Int>

}