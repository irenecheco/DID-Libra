package it.polito.s279941.libra.DataModel

import com.google.gson.annotations.SerializedName
import java.util.*

data class UtenteLoginData(
        var email: String ="",
        var password: String =""
)

data class PazienteData(
        var pazienteId: String = ""
)

// TODO: verificare coerenza con attributi definiti in NodeJS
data class UtenteSigninData(
        var tipo: String ="PAZ", //  PAZ(default)|NUT
        var email: String ="",
        var password: String ="",
        var nome: String ="",
        var cognome: String ="",
        var data_nascita: Date? = null, // todo: check with date format in DB
        var data_iscrizione: Date? = null,
        var cod_nutrizionista: String ="" // valido sia per pazienti che per nutrizionisti
)

// TODO: aggiungere attributi mancanti come definiti nel server NodeJS
data class UtenteDataClass(
        var id: String ="",
        var _id: String ="",
        var tipo: String ="",
        var data_iscrizione: Date? = null, // todo: check with date format in DB
        var email: String ="",
        var password: String ="",
        var passwordHash: String ="",
        var salt: String ="",
        var nome: String ="",
        var cognome: String ="",
        var data_nascita: Date? = null, // todo: check with date format in DB
        var cod_nutrizionista: String ="", // valido sia per pazienti che per nutrizionisti
        //var cod_iscrizione_albo: String ="",  // OBSOLETO: ELIMINATO e sostituito con "cod_nutrizionista"
        var lista_pazienti: List<Paziente>? = null,
        var scr_foto: String ="",
        var obiettivi: MutableList<Obiettivo>? = null,
        var storico_pesi: MutableList<Peso>? = null,
        var dieta: Dieta? = null,
        var calendarioDieta: MutableList<CalendarioDieta>? = null,
        var registrazione_peso: Int =0
)


data class CalendarioDieta(
        var data: String? = null,
        var commento: String? = null,
        var consumazionePasto: ConsumazionePasto? = null
)
data class  ConsumazionePasto(
        var colazione: Boolean = false,
        var spuntinoMattina: Boolean = false ,
        var pranzo: Boolean = false,
        var spuntinoPomeriggio: Boolean = false,
        var cena: Boolean = false
)


data class Obiettivo(
        @SerializedName("data_obiettivo")
        val data_obiettivo: Date,
        @SerializedName("obiettivo")
        val obiettivo: String,
)

data class Paziente(
        // nome attributo nel db NodeJS
        @SerializedName("_id")
        // nome riassegnato in kotlin
        val IdPaziente: String
)

data class PazientiItem(
        val id: String,
        val nome_paziente: String,
        val cognome_paziente: String
)

data class Peso(
        @SerializedName("data") var data: Date?,
        @SerializedName("peso") var peso: Double?,
)


data class Dieta(
        var data_inizio: String?,
        var giorni: MutableList<GiornoDieta>,
)

data class GiornoDieta(
        var nroGiorno: Int,
        var colazione: String?,
        var spuntinoMattina: String?,
        var pranzo: String?,
        var spuntinoPomeriggio: String?,
        var cena: String?,
)


// =====================================================================================================================================
// Classi accessorie per passare alcune informazioni al server
// =====================================================================================================================================
data class CommentoDietaPerUpdateDB (var commento: String) // Per aggiornare le note del giorno in una dieta dell'utente
data class CheckPastoPerUpdateDB (var pastoConsumato: Boolean) // Per aggiornare il pasto consumeto o meno in una dieta dell'utente
// =====================================================================================================================================
