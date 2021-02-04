package android.exemple.com.pokedex.retrofit

data class PokemonInformation (var height : Double, var weight : Double, var types : List<Types>)

data class Types (var slot : Int,var type : Type)

data class Type( var name: String)


data class PokemonInformationSuite (var names : List<Names>,var flavor_text_entries : List<Flavor>)

data class Names (var name : String,var language: LangageP)

data class Flavor(var flavor_text : String,var language : Langage)

data class Langage(var name: String)

data class LangageP(var name: String)

