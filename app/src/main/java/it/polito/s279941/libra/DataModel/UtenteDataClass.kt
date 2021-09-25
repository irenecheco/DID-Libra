package it.polito.s279941.libra.DataModel

import com.google.gson.annotations.SerializedName
import java.util.*

data class UtenteLoginData(
        var email: String ="",
        var password: String =""
)

// TODO: aggiungere attributi mancanti come definiti nel server NodeJS
data class UtenteDataClass(
        var id: String ="",
        var tipo: String ="",
        var data_iscrizione: Date? = null, // todo: check with date format in DB
        var email: String ="",
        var password: String ="",
        var passwordHash: String ="",
        var salt: String ="",
        var nome: String ="",
        var cognome: String ="",
        var data_nascita: Date? = null, // todo: check with date format in DB
        var cod_iscrizione_albo: String ="",
        var lista_pazienti: List<String>? = null,
        var scr_foto: String ="",
        var obiettivi: List<Obiettivo>? = null,
        var storico_pesi: List<Peso>? = null,
        var dieta: Dieta? = null,
        var registrazione_peso: Int =0
)


data class Obiettivo(
        //@SerializedName("_id")
        //var id: String,
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
