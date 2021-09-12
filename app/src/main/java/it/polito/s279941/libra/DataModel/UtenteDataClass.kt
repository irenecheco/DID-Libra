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
        val cod_iscrizione_albo: String,
        val lista_pazienti: List<String>,
        val scr_foto: String,
        val obiettivi: List<Obiettivo>,
        val storico_pesi: List<Peso>,
        val dieta: List<Dieta>,
        val registrazione_peso: Int,
)


data class Obiettivo(
        val data: Date,
        val obiettivo: String,
)


data class Peso(
        val data: Date,
        val peso: Double,
)


data class Dieta(
        val data_inizio: Date,
        val giorni: List<GiornoDieta>,
)


data class GiornoDieta(
        val nroGiorno: Int,
        val colazione: String,
        val spuntinoMattina: String,
        val pranzo: String,
        val spuntinoPomeriggio: String,
        val cena: String,
)
