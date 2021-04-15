package it.polito.s279941.libra.DataModel

// TODO: aggiungere attributi mancanti

data class UtenteDataClass(
    val tipo: String,
    val data_iscrizione: String, // to FIX with date format
    val email: String,
    val password: String,
    val passwordHash: String,
    val salt: String,
    val nome: String,
    val cognome: String,
    val data_nascita: String // to FIX with date format
)

/*
object Model {
    data class Result(val query: Query)
}*/
