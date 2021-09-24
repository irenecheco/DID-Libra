package it.polito.s279941.libra.DataModel

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class UtenteLoginData(
        var email: String ="",
        var password: String =""
)

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
        val dieta: Dieta,
        val registrazione_peso: Int,
)


data class Obiettivo(
        @SerializedName("_id")
        var id: String,
        @SerializedName("data_obiettivo")
        val data_obiettivo: Date,
        @SerializedName("obiettivo")
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
