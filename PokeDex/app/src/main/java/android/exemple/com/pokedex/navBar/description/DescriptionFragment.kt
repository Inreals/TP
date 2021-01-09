package android.exemple.com.pokedex.navBar.description
//Enregistrer les infos dans la db et check si c'est présent
//colour accent des bar
//numero du pokemon
//bouton retour en haut

import android.exemple.com.pokedex.R
import android.exemple.com.pokedex.navBar.PokemonDescription
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*


class DescriptionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        UpdateUi()
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    private  fun UpdateUi() //Aucune idée pourquoi mais sans préciser la coroutine impossible d'afficher quoi que ce soit avec cette fonction
    {
        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
        uiScope.launch {
            withContext(Dispatchers.Main)
            {
        view?.findViewById<TextView>(R.id.types)?.text = PokemonDescription.types
        view?.findViewById<TextView>(R.id.taille)?.text = PokemonDescription.taille
        view?.findViewById<TextView>(R.id.poids)?.text = PokemonDescription.poids
        view?.findViewById<TextView>(R.id.description)?.text = PokemonDescription.description}}
    }


}

