package it.polito.s279941.libra.utente

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.*
import it.polito.s279941.libra.utentedieta.PastoItem
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UtenteViewModel: ViewModel() {

    private var _userData: UtenteDataClass = UtenteDataClass()

    private var _giorno: Long = System.currentTimeMillis()

    private var _pastiDelGiorno = listOf(
        PastoItem("COLAZIONE", "", false),
        PastoItem("SPUNTINO", "", false),
        PastoItem("PRANZO", "", false),
        PastoItem("MERENDA", "", true),
        PastoItem("CENA", "", false)
    )

    /**
     * Quando viene letto l'utente occorre che qualcuno richiami questo metodo
     *
     */
    fun initByUtenteDataClass(userData: UtenteDataClass){
        // TODO: Quando viene letto l'utente occorre che qualcuno richiami questo metodo !!!!
        _userData=userData;


        if (userData.dieta==null) {
            userData.dieta = Dieta("2021-10-09",
                mutableListOf<GiornoDieta>(
                    GiornoDieta(0,"briosche","biscotto","spaghetti","yogurt","mela") ,
                    GiornoDieta(1,"marmellata","tost","ragu","uva","pizza"),
                    GiornoDieta(2,"nutella","caffe","lasagne","torta","frittura di pesce")
            )
            )
            userData.calendarioDieta = mutableListOf();
        }

        setGiorno(System.currentTimeMillis())

    }



    private val _noteDelGiornoLiveData = MutableLiveData<String>().also{it.value = ""}
    val noteDelGiornoLiveData : LiveData<String> = _noteDelGiornoLiveData
    fun setNoteDelGiorno(note:String ){
        _noteDelGiornoLiveData.value = note

    }
    fun saveNoteGiornoToDB(note:String){
        setNoteDelGiorno(note)
        var currDayCalDieta= getDietaDelGiornoSeEsisteOCrealaNuova(_giorno)
        currDayCalDieta.commento = note

        // TODO: Salvarle anche sul database
        // POST http://localhost:3000/api/users/set-comment/6071aea342e7530e8c1947ed/2020-04-23
        // content-type: application/json
        //{"commento": "Mangia benissimo il 23"}
    }
    fun savePastoConsumatoToDB(nomePasto:String,consumato: Boolean){

        var currDayCalDieta= getDietaDelGiornoSeEsisteOCrealaNuova(_giorno)
        when (nomePasto) {
            "COLAZIONE" -> {
                _pastiDelGiorno[0].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.colazione = consumato
            }
            "SPUNTINO" -> {
                _pastiDelGiorno[1].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.spuntinoMattina = consumato
            }
            "PRANZO" -> {
                _pastiDelGiorno[2].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.pranzo = consumato
            }
            "MERENDA" -> {
                _pastiDelGiorno[3].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.spuntinoPomeriggio = consumato
            }
            "CENA" -> {
                _pastiDelGiorno[4].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.cena = consumato
            }
            else -> { // Note the block
                Log.e("nome pasto non gestito",nomePasto)
            }

        }

        // TODO: Salvare anche sul database
        // POST http://localhost:3000/api/users/set-check-pasto/6071aea342e7530e8c1947ed/2020-04-23/pranzo
        // content-type: application/json
        // { "pastoConsumato": true }
    }

    private val _giornoLiveData = MutableLiveData<Long>().also{it.value = _giorno}
    val giornoLiveData : LiveData<Long> = _giornoLiveData



    private val _pastiDelGiornoLiveData =
            MutableLiveData<List<PastoItem>>().also{it.value = _pastiDelGiorno}

    val pastiDelGiornoLiveData : LiveData<List<PastoItem>> =_pastiDelGiornoLiveData

    fun setPastiDelGiorno(pastiDelGiorno:List<PastoItem> ){
        //_giorno= giorno
        //_giornoLiveData.value=_giorno
        _pastiDelGiorno=pastiDelGiorno
        _pastiDelGiornoLiveData.value=_pastiDelGiorno
    }
    fun getGiorno():Long{
        return _giorno
    }
    fun setGiorno(giorno:Long) {
        // Richiamato quando vengono passati i dati caricati dal DB (nel metodo initByUtenteDataClass)
        // e poi ogni volta che l'utente cambia il giorno a partire dal calendario.
        // Deve andare a recuperare il giorno all' interno della struttura _userData
        // e trasformarlo nel formato adatto per la recicleview
        // se all'interno di _userData non esiste ancora il giorno indicato allora lo crea nuovo
        // in base al corrispondente giorno di dieta che ha compilato il dietologo


        _giorno = giorno

        var currDayCalDieta = getDietaDelGiornoSeEsisteOCrealaNuova(giorno)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val fromDate = sdf.parse(_userData.dieta?.data_inizio)
        val toDate = sdf.parse(currDayCalDieta.data)
        val diffInDay = TimeUnit.DAYS.convert(toDate.getTime() - fromDate.getTime(), TimeUnit.MILLISECONDS)

        val resto = diffInDay % _userData.dieta?.giorni?.size!!
        Log.d("aaaa","Nro Giornidtra le date:"+fromDate+" - "+toDate+" - " +diffInDay+ "=>Resto:"+resto)

        var dietaDelGiorno = _userData.dieta?.giorni!![resto.toInt()]


        // Carichiamo i pasti del giorno
        _pastiDelGiorno[0].descrizione   = dietaDelGiorno.colazione
        _pastiDelGiorno[0].ho_rispettato = currDayCalDieta.consumazionePasto!!.colazione

        _pastiDelGiorno[1].descrizione = dietaDelGiorno.spuntinoMattina
        _pastiDelGiorno[1].ho_rispettato = currDayCalDieta.consumazionePasto!!.spuntinoMattina

        _pastiDelGiorno[2].descrizione = dietaDelGiorno.pranzo
        _pastiDelGiorno[2].ho_rispettato = currDayCalDieta.consumazionePasto!!.pranzo

        _pastiDelGiorno[3].descrizione = dietaDelGiorno.spuntinoPomeriggio
        _pastiDelGiorno[3].ho_rispettato = currDayCalDieta.consumazionePasto!!.spuntinoPomeriggio

        _pastiDelGiorno[4].descrizione = dietaDelGiorno.cena
        _pastiDelGiorno[4].ho_rispettato = currDayCalDieta.consumazionePasto!!.cena

        setNoteDelGiorno(currDayCalDieta.commento?:"")

        _giornoLiveData.value=_giorno
        setPastiDelGiorno(_pastiDelGiorno)
    }

    private fun getDietaDelGiornoSeEsisteOCrealaNuova(giorno: Long): CalendarioDieta {
        var d= Date(giorno)
        val dataDelGiorno = (d.year+1900).toString() +"-"+ (if (d.month<9) "0" else "") + (d.month+1)+"-" + (if (d.date>9) "" else "0")+ d.date
        // DataOdierna sarà nel formato YYYY-MM-GG

        // impostiamo le note del giorno individuato precedentemente
        var currDayCalDieta = _userData.calendarioDieta?.find { el -> el.data.equals(dataDelGiorno) }
        if (currDayCalDieta==null) {
            currDayCalDieta = CalendarioDieta(dataDelGiorno,"",
                ConsumazionePasto()
            )
            _userData!!.calendarioDieta?.add(currDayCalDieta)
        }
        return currDayCalDieta
    }

}