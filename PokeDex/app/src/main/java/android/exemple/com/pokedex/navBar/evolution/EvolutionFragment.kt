package android.exemple.com.pokedex.navBar.evolution

import android.content.Intent
import android.exemple.com.pokedex.IdCompanion
import android.exemple.com.pokedex.R
import android.exemple.com.pokedex.database.PokemonDatabase
import android.exemple.com.pokedex.navBar.PokemonDescription
import android.exemple.com.pokedex.retrofit.PokeApi1Service
import android.exemple.com.pokedex.retrofit.PokemonEvolutions
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Predicate


class EvolutionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        getEvolution()
        return inflater.inflate(R.layout.fragment_evolution, container, false)

    }

    private fun getEvolution() {
        val service = Retrofit.Builder()
            .baseUrl(PokeApi1Service.ENDPOINT2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi1Service::class.java)


        service.getEvolution(IdCompanion.id).enqueue(object : Callback<List<PokemonEvolutions>> {
            override fun onFailure(call: Call<List<PokemonEvolutions>>, t: Throwable) {
                Log.i("Error", t.toString())
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<List<PokemonEvolutions>>, response: Response<List<PokemonEvolutions>>) {
                if(response.body()?.get(0)!=null)
                {
                var listeEvolutionsTemp: ArrayList<String> = ArrayList()
                listeEvolutionsTemp.addAll(response.body()?.get(0)?.family?.evolutionLine!!)
                listeEvolutionsTemp.replaceAll(String::toLowerCase)

                var listeEvolutions : ArrayList<String> = ArrayList()

                for(pokemon in listeEvolutionsTemp) //certaines evolution sont regroupées au sein d'une même chaine de caractère comme évoli#133 par exemple
                {
                    listeEvolutions.addAll(pokemon.split("/"))
                }

                Log.i("EvolutionFragment", "Liste evolution delimitée " + listeEvolutions.toString())

                val viewModelJob = Job()
                val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
                val db = context?.let { PokemonDatabase.getInstance(it) }

                uiScope.launch {
                    val listePokemon: ArrayList<Int> = (db?.pokemonDatabaseDao?.getPokemonId(listeEvolutions) as ArrayList<Int>?)!!

                    var itself = Predicate { id: Int -> id <= IdCompanion.id!! }
                    listePokemon.removeIf(itself)
                    Log.i("EvolutionFragment", "Liste evolution à afficher " + listePokemon.toString())
                    withContext(Dispatchers.Main)
                    {
                        if(listePokemon.isEmpty())
                        {
                            view?.findViewById<TextView>(R.id.noEvolution)?.visibility=View.VISIBLE
                        }
                        else {
                            var rvPokemon = view?.findViewById<RecyclerView>(R.id.listageEvolution)
                            val adapter = EvolutionAdapter(listePokemon, this@EvolutionFragment)
                            { pokemonID ->
                                val intent = Intent(context, PokemonDescription::class.java)
                                IdCompanion.id = pokemonID
                                startActivity(intent)
                            }
                            rvPokemon?.adapter = adapter
                            rvPokemon?.layoutManager = GridLayoutManager(context, 3)
                        }
                    }
                }

            }
            else
                {
                    Toast.makeText(context, "Le chargement des évolutions de cette génération sont indisponibles", Toast.LENGTH_LONG).show()
                }}
        })
    }
}