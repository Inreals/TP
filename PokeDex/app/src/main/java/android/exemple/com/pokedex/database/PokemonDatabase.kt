package android.exemple.com.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract val pokemonDatabaseDao: PokemonDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getInstance(context: Context): PokemonDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDatabase::class.java,
                        "pokemon_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}