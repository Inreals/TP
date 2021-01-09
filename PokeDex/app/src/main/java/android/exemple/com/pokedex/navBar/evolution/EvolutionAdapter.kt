package android.exemple.com.pokedex.navBar.evolution

import android.exemple.com.pokedex.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class EvolutionAdapter(private val listeEvolution: List<Int>, var mycontext: EvolutionFragment, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<EvolutionAdapter.MyViewHolder>() {


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

        Glide.with(mycontext).load("https://pokeres.bastionbot.org/images/pokemon/"+listeEvolution[position]+".png").placeholder(R.drawable.gen_selected).into(holder.pokemon)
        holder.itemView.setOnClickListener{listener(listeEvolution[position])}

    }

    override fun getItemCount() = listeEvolution.size
}