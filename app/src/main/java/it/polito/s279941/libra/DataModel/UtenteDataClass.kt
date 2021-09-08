package it.polito.s279941.libra.DataModel

import java.util.*

// TODO: aggiungere attributi mancanti come definiti nel server NodeJS

data class UtenteDataClass(
        val id: String,
        val tipo: String,
        val data_iscrizione: Date, // todo: check with date format in DB
        val email: String,
        val password: String,
        val passwordHash: String,
        val salt: String,
        val nome: String,
        val cognome: String,
        val data_nascita: Date, // todo: check with date format in DB
)

/*
object Model {
    data class Result(val query: Query)
}*/
