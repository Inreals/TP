package android.exemple.com.pokedex

import android.content.Context
import android.exemple.com.pokedex.database.Pokemon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



class PokemonAdapter (private val listePokemon: List<Pokemon>, var mycontext: Context,private val listener : (Pokemon) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var pokemon = view.findViewById<ImageView>(R.id.pokemon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false) as LinearLayout

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(mycontext).load("https://pokeres.bastionbot.org/images/pokemon/"+listePokemon[position].pokemonId+".png").placeholder(R.drawable.gen_selected).into(holder.pokemon)
        holder.itemView.setOnClickListener{listener(listePokemon[position])}

    }

    override fun getItemCount() = listePokemon.size
}