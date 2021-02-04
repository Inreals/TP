package android.exemple.com.pokedex

import android.content.Intent
import android.exemple.com.pokedex.database.Pokemon
import android.exemple.com.pokedex.database.PokemonDatabase
import android.exemple.com.pokedex.navBar.PokemonDescription
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var previousId: Int = 0
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var pokemonList: List<Pokemon> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.gen1_pokemon) // A l'init de la page on change le titre sur 1ere gen pok
        getPokemonGen(findViewById<View>(R.id.gen1)) //on highlight la poke ball gen1
        getPokemonDB(1) //Load par defaut
    }

    fun getPokemonGen(view: View) {
        if (previousId != view.id) { //je vérifie que l'id précédent et différent du nouveau pour éviter une requête inutile
            if (previousId != 0) //évite de faire crash le programme lors du premier clic au démarrage de l'app
            {
                val previousButton = findViewById<ImageView>(previousId)
                previousButton.isActivated = !previousButton.isActivated
            }
            when (view.id) {
                R.id.gen1 -> setImage(view, R.string.gen1_pokemon, 1)
                R.id.gen2 -> setImage(view, R.string.gen2_pokemon, 2)
                R.id.gen3 -> setImage(view, R.string.gen3_pokemon, 3)
                R.id.gen4 -> setImage(view, R.string.gen4_pokemon, 4)
                R.id.gen5 -> setImage(view, R.string.gen5_pokemon, 5)
                R.id.gen6 -> setImage(view, R.string.gen6_pokemon, 6)
                R.id.gen7 -> setImage(view, R.string.gen7_pokemon, 7)
                R.id.gen8 -> setImage(view, R.string.gen8_pokemon, 8)
            }
        }
    }

    private fun setImage(view: View, i: Int, gen: Int) {

        view.isActivated = !view.isActivated
        title = getString(i)
        previousId = view.id
        getPokemonDB(gen)

    }

    private fun getPokemonDB(gen: Int) {
        val db = PokemonDatabase.getInstance(this)

        uiScope.launch {
            withContext(Dispatchers.IO)
            {
                pokemonList = db.pokemonDatabaseDao.getPokemonGen(gen)

            }
            Log.i("Main", "Nombre de pokemon dans cette gen " + pokemonList.size.toString())
            var rvPokemon = findViewById<RecyclerView>(R.id.listagepokemon)
            val adapter: PokemonAdapter = PokemonAdapter(pokemonList, this@MainActivity)
            { pokemonid ->
                Log.i("Pos",pokemonid.toString())
                val intent = Intent(this@MainActivity, PokemonDescription::class.java)
                IdCompanion.id = pokemonid
                startActivity(intent)

            }
            rvPokemon.adapter = adapter
            rvPokemon.layoutManager = GridLayoutManager(this@MainActivity, 3)

        }
    }

}