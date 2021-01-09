package android.exemple.com.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class Pokemon(
    @PrimaryKey(autoGenerate = false)
    var pokemonId: Int,

    @ColumnInfo(name = "generationId")
    val generationId: Int,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "nom")
    val nom: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "taille")
    val taille: String?,

    @ColumnInfo(name = "poids")
    val poids: String?,

    @ColumnInfo(name = "types")
    val types: String?










)
