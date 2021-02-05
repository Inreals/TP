package android.exemple.com.pokedex.navBar

import android.exemple.com.pokedex.IdCompanion
import android.exemple.com.pokedex.R
import android.exemple.com.pokedex.database.PokemonDatabase
import android.exemple.com.pokedex.retrofit.PokeApi1Service
import android.exemple.com.pokedex.retrofit.PokemonInformation
import android.exemple.com.pokedex.retrofit.PokemonInformationSuite
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDescription : AppCompatActivity() {
    companion object {
        var types = ""
        var taille = ""
        var poids = ""
        var description = ""
        var nom = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PokemonInDb()
        title = "Pokemon#" + IdCompanion.id


        setContentView(R.layout.activity_pokemon_description)
        val imagePokemon = findViewById<ImageView>(R.id.imagePokemon)
        Glide.with(this).load("https://pokeres.bastionbot.org/images/pokemon/" + IdCompanion.id + ".png").into(imagePokemon!!)



        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    private fun PokemonInDb() {

        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
        val db = PokemonDatabase.getInstance(this)

        uiScope.launch {
            val pokemon = db.pokemonDatabaseDao.pokemonInDb(IdCompanion.id!!)
            withContext(Dispatchers.Main)
            {
                if (pokemon.description != null && pokemon.taille != null && pokemon.nom != null && pokemon.types != null && pokemon.poids != null) {

                    types = pokemon.types
                    taille = pokemon.taille
                    poids = pokemon.poids
                    nom = pokemon.nom
                    description = pokemon.description

                    findViewById<TextView>(R.id.nom).text = nom
                    updateColorAccent(pokemon.types.split(" ")[0])

                } else {

                    retrofitCall()
                }


            }
        }
    }

    private fun retrofitCall() {

        val service = Retrofit.Builder()
            .baseUrl(PokeApi1Service.ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi1Service::class.java)

        service.getPokemonDescription(IdCompanion.id).enqueue(object : Callback<PokemonInformation> {
            override fun onFailure(call: Call<PokemonInformation>, t: Throwable) {
                Log.i("Error", t.toString())
                Toast.makeText(this@PokemonDescription, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }


            override fun onResponse(
                call: Call<PokemonInformation>,
                response: Response<PokemonInformation>
            ) {
                val pokemonInformation = response.body()
                types = ""

                if (pokemonInformation != null) {
                    taille = (pokemonInformation.height/10).toString() + " m"
                    poids = (pokemonInformation.weight/10).toString() + " kg"
                    for (type in pokemonInformation.types) {
                        types += type.type.name + " "
                    }
                    findViewById<TextView>(R.id.types).text = types
                    findViewById<TextView>(R.id.taille).text = taille
                    findViewById<TextView>(R.id.poids).text = poids
                    service.getPokemonDescriptionSuite(IdCompanion.id).enqueue(object : Callback<PokemonInformationSuite> {
                        override fun onFailure(call: Call<PokemonInformationSuite>, t: Throwable) {
                            Log.i("Error", t.toString())
                            Toast.makeText(this@PokemonDescription, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }


                        override fun onResponse(call: Call<PokemonInformationSuite>, response: Response<PokemonInformationSuite>) {
                            val pokemonInformationSuite = response.body()

                            if (pokemonInformationSuite != null) {

//
                                var i = 0
                                while (pokemonInformationSuite.flavor_text_entries[i].language.name != "fr") {
                                    i++
                                }
                                var j = 0
                                while(pokemonInformationSuite.names[j].language.name != "fr")
                                {
                                    j++
                                }

                                nom = pokemonInformationSuite.names[j].name
                                description = pokemonInformationSuite.flavor_text_entries[i].flavor_text
                                Log.i("PokemonDescription", "Description du pokemon" + description)
                                findViewById<TextView>(R.id.nom).text = nom
                                findViewById<TextView>(R.id.description).text = description
                                updatePokemonInfo()


                            } else {
                                Toast.makeText(
                                    this@PokemonDescription,
                                    "Erreur de récupération des infos pokemon2",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                    })

                    updateColorAccent(types.split(" ")[0])
                } else {
                    Toast.makeText(
                        this@PokemonDescription,
                        "Erreur de récupération des infos pokemon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })


    }

    private fun updatePokemonInfo() {
        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val db = PokemonDatabase.getInstance(this)
        uiScope.launch {
            withContext(Dispatchers.IO)
            {

                db.pokemonDatabaseDao.updatePokemonInfo(IdCompanion.id!!, nom, description, taille, poids, types)

            }
        }
    }

    private fun updateColorAccent(type: String) {
        fun setColor(R: Int, G: Int, B: Int) {
            findViewById<BottomNavigationView>(android.exemple.com.pokedex.R.id.nav_view).setBackgroundColor(Color.rgb(R, G, B))
            findViewById<ImageView>(android.exemple.com.pokedex.R.id.couleur).setBackgroundColor(Color.rgb(R, G, B))
        }
        when (type) {
            "normal" -> setColor(128, 128, 128)
            "fire" -> setColor(240, 100, 10)
            "water" -> setColor(0, 0, 128)
            "grass" -> setColor(0, 100, 0)
            "electric" -> setColor(231, 236, 19)
            "ice" -> setColor(55, 179, 185)
            "fighting" -> setColor(185, 0, 0)
            "poison" -> setColor(128, 0, 128)
            "ground" -> setColor(128, 128, 0)
            "flying" -> setColor(168, 144, 240)
            "psychic" -> setColor(248, 88, 136)
            "bug" -> setColor(168, 184, 32)
            "rock" -> setColor(184, 160, 56)
            "ghost" -> setColor(110, 90, 150)
            "dark" -> setColor(110, 90, 70)
            "dragon" -> setColor(110, 55, 250)
            "steel" -> setColor(185, 185, 210)
            "fairy" -> setColor(240, 180, 190)
        }

    }

}