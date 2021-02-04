package android.exemple.com.pokedex

///////PAS OUBLIER QUE LA LISTE DE POKEMON EST LIMITE(2gen chargés) POUR EVITER DE SURCHARGEAPI
//VOIR loadAllPokemonGeneration dans le for
/////////////////////////////////////////////////////////////////////////////////
import android.content.Intent
import android.exemple.com.pokedex.database.Pokemon
import android.exemple.com.pokedex.database.PokemonDatabase
import android.exemple.com.pokedex.retrofit.GenCount
import android.exemple.com.pokedex.retrofit.PokeApi1Service
import android.exemple.com.pokedex.retrofit.PokemonSpecy
import android.exemple.com.pokedex.retrofit.PokemonSpecyList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class Loading : AppCompatActivity() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var pokemonListe: ArrayList<PokemonSpecy> = ArrayList() //liste plus trop utile voir plus bas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)
        val intent = Intent(this, MainActivity::class.java)



        if (getDatabasePath("pokemon_database").exists()) {
            startActivity(intent)
            finish()
        } else {

            val service = Retrofit.Builder()
                .baseUrl(PokeApi1Service.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokeApi1Service::class.java)


            service.generationCount().enqueue(object : Callback<GenCount> {
                override fun onFailure(call: Call<GenCount>, t: Throwable) {
                    Log.i("Error", t.toString())
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<GenCount>, response: Response<GenCount>) {
                    Log.i("Loading", response.body().toString())
                    loadAllPokemonGeneration(service, response.body())

                    Timer("LoadingScreen", false).schedule(5000) {
                        Log.i("Loading", "Nombre de pokemon " + pokemonListe.size.toString())
                        startActivity(intent)
                        finish()

                    }
                }
            })


        }
    }

    fun loadAllPokemonGeneration(service: PokeApi1Service, gencount: GenCount?) {
        val db = PokemonDatabase.getInstance(this)
        val temp: ArrayList<PokemonSpecy> = ArrayList()
        for (i in 1..gencount?.count!!) {
            service.listePokemonGenN(i).enqueue(object : Callback<PokemonSpecyList> {
                override fun onFailure(call: Call<PokemonSpecyList>, t: Throwable) {
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<PokemonSpecyList>,
                    response: Response<PokemonSpecyList>
                ) {
                    temp.addAll(response.body()?.pokemon_species!!)
                    for (pokemon in temp) {
                        pokemon.id = pokemon.url.substring(42, pokemon.url.length - 1).toInt()
                        pokemon.generation = i
                    }
                    pokemonListe.addAll(temp) // Utile pour debug

                    val map = temp.map {
                        Pokemon(it.id, it.generation, it.name, null, null, null, null, null)

                    }
                    temp.clear()
                    uiScope.launch {
                        withContext(Dispatchers.IO)
                        {
                            db.pokemonDatabaseDao.insert(map)
                        }
                    }

                }
            })
        }
    }
}
