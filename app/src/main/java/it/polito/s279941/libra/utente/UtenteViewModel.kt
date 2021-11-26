package it.polito.s279941.libra.utente

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.*
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utentedieta.PastoItem
import it.polito.s279941.libra.utentedieta.UtenteDietaRepository
import it.polito.s279941.libra.utils.LOG_TAG
import it.polito.s279941.libra.utils.Status
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UtenteViewModel: ViewModel() {

    val restApiManager = RestApiManager()
    val utenteDietaRepository = UtenteDietaRepository(restApiManager)

    private var _userData: UtenteDataClass = UtenteDataClass()

    private var _giorno: Long = System.currentTimeMillis()

    private var _pastiDelGiorno = listOf(
        PastoItem("COLAZIONE", "", false),
        PastoItem("SPUNTINO", "", false),
        PastoItem("PRANZO", "", false),
        PastoItem("MERENDA", "", false),
        PastoItem("CENA", "", false)
    )

    /** @AG **/
    // var che contiene l'oggetto utente (che ha fatto login/signin) passato dall'activity di login/signin
    var utenteCorrente : UtenteDataClass = UtenteDataClass()
    // funzione di test per verificare se effettivamente l'oggetto c'è
    fun initByUtenteDataClass_AG(userData: UtenteDataClass){
        Log.d(LOG_TAG, "initByUtenteDataClass_AG() tipologia utente: ${utenteCorrente.tipo} , email: ${utenteCorrente.email}  in UtenteViewModel")
        setPaziente(userData._id)
        //initByUtenteDataClass(userData)
    }


    /**
     * Quando viene letto l'utente occorre che qualcuno richiami questo metodo
     *
     */
    fun setPaziente(idPaziente: String){

        // GET http://localhost:3000/api/users/{idUtente} // 6071aea342e7530e8c1947ed
        //"6071aea342e7530e8c1947ed"
        utenteDietaRepository.getPaziente(idPaziente) {
            //paziente.value = _PazienteLiveData.value
            //_paziente = it // _PazienteLiveData.value ?: UtenteDataClass()
            if (it!=null) initByUtenteDataClass(it)
        }
    }

        /**
     * Quando viene letto l'utente occorre che qualcuno richiami questo metodo
     *
     */
    fun initByUtenteDataClass(userData: UtenteDataClass){

        _userData=userData;

        if (userData.dieta==null) {
            userData.dieta = Dieta("2021-01-01",
                mutableListOf<GiornoDieta>(
                    GiornoDieta(0,"Dieta non ancora assegnata dal nutrizionista","Dieta non ancora assegnata dal nutrizionista","Dieta non ancora assegnata dal nutrizionista","Dieta non ancora assegnata dal nutrizionista","Dieta non ancora assegnata dal nutrizionista")
                )
            )
        }
        if (userData.dieta!!.data_inizio==null) {
            userData.dieta!!.data_inizio = "2021-01-01"
        }
        if (userData.dieta!!.giorni==null || userData.dieta!!.giorni.size==0) {
            val msg = "Dieta non ancora assegnata dal nutrizionista" // String = Resources.getSystem().getString(R.string.utente_messaggio_dieta_not_assegnata)
            userData.dieta!!.giorni = mutableListOf<GiornoDieta>(
                GiornoDieta(0,msg,msg,msg,msg,msg)
            )
        }
        if (userData.calendarioDieta == null) {
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
        var d= Date(_giorno)
        val dataDelGiorno = (d.year+1900).toString() +"-"+ (if (d.month<9) "0" else "") + (d.month+1)+"-" + (if (d.date>9) "" else "0")+ d.date
        val commentoJson = CommentoDietaPerUpdateDB(note)
        utenteDietaRepository.saveCommento(_userData._id, dataDelGiorno, commentoJson,)
        // content-type: application/json
        //{"commento": "Mangia benissimo il 23"}
    }

    fun savePastoConsumatoToDB(nomePasto:String,consumato: Boolean){

        var currDayCalDieta= getDietaDelGiornoSeEsisteOCrealaNuova(_giorno)
        var nomePastoDB: String
        when (nomePasto) {
            "COLAZIONE" -> {
                nomePastoDB = "colazione"
                _pastiDelGiorno[0].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.colazione = consumato
            }
            "SPUNTINO" -> {
                nomePastoDB = "spuntinoMattina"
                _pastiDelGiorno[1].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.spuntinoMattina = consumato
            }
            "PRANZO" -> {
                nomePastoDB = "pranzo"
                _pastiDelGiorno[2].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.pranzo = consumato
            }
            "MERENDA" -> {
                nomePastoDB ="spuntinoPomeriggio"
                _pastiDelGiorno[3].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.spuntinoPomeriggio = consumato
            }
            "CENA" -> {
                nomePastoDB ="cena"
                _pastiDelGiorno[4].ho_rispettato = consumato
                currDayCalDieta.consumazionePasto!!.cena = consumato
            }
            else -> { // Note the block
                Log.e("nome pasto non gestito",nomePasto)
                return;
            }

        }

        // TODO: Salvare anche sul database
        var d= Date(_giorno)
        val dataDelGiorno = (d.year+1900).toString() +"-"+ (if (d.month<9) "0" else "") + (d.month+1)+"-" + (if (d.date>9) "" else "0")+ d.date
        val checkJson = CheckPastoPerUpdateDB(consumato)
        utenteDietaRepository.saveCheckPasto(_userData._id, dataDelGiorno, nomePastoDB, checkJson,)

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
        if (diffInDay<0) {
            // La data inizio dieta è successiva ad oggi, non deve mostrare la dieta
            _pastiDelGiorno[0].descrizione = "Data inizio dieta successiva alla data selezionata"
            _pastiDelGiorno[1].descrizione = "Data inizio dieta successiva alla data selezionata"
            _pastiDelGiorno[2].descrizione = "Data inizio dieta successiva alla data selezionata"
            _pastiDelGiorno[3].descrizione = "Data inizio dieta successiva alla data selezionata"
            _pastiDelGiorno[4].descrizione = "Data inizio dieta successiva alla data selezionata"
        } else {
            val resto = diffInDay % _userData.dieta?.giorni?.size!!
            Log.d(
                "aaaa",
                "Nro Giornidtra le date:" + fromDate + " - " + toDate + " - " + diffInDay + "=>Resto:" + resto
            )

            var dietaDelGiorno = _userData.dieta?.giorni!![resto.toInt()]

            // Carichiamo i pasti del giorno
            _pastiDelGiorno[0].descrizione = dietaDelGiorno.colazione ?: ""
            _pastiDelGiorno[0].ho_rispettato = currDayCalDieta.consumazionePasto?.colazione ?:false

            _pastiDelGiorno[1].descrizione = dietaDelGiorno.spuntinoMattina ?: ""
            _pastiDelGiorno[1].ho_rispettato = currDayCalDieta.consumazionePasto?.spuntinoMattina?:false

            _pastiDelGiorno[2].descrizione = dietaDelGiorno.pranzo ?: ""
            _pastiDelGiorno[2].ho_rispettato = currDayCalDieta.consumazionePasto?.pranzo?:false

            _pastiDelGiorno[3].descrizione = dietaDelGiorno.spuntinoPomeriggio ?: ""
            _pastiDelGiorno[3].ho_rispettato = currDayCalDieta.consumazionePasto?.spuntinoPomeriggio?:false

            _pastiDelGiorno[4].descrizione = dietaDelGiorno.cena ?: ""
            _pastiDelGiorno[4].ho_rispettato = currDayCalDieta.consumazionePasto?.cena?:false
        }
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

    /** alcune funzioni di utilità per DBG etc..  ***/

    fun getTipo() : String {
        return _userData.tipo
    }

    fun getEmail() : String {
        return _userData.email
    }


    /**
     * VIEWMODEL PER BILANCIA
     *
     */

    var userWeight = Peso(
        data = null,
        peso = null
    )

    var confirmation: MutableLiveData<Status> = MutableLiveData<Status>()

    fun postWeight(new_date: Date, new_weight: Double){

        confirmation.setValue(Status.LOADING)

        userWeight.data = new_date
        userWeight.peso = new_weight
        var id = utenteCorrente._id

        Log.d("LIBRA", "start fun postWeight() in class UtenteViewModel")
        Log.d("LIBRA", "attuale valore di userWeight: data " + userWeight.data + ", peso " + userWeight.peso )

        restApiManager.postWeight(id, userWeight) {
            Log.d(
                "LIBRA",
                "start fun restApiManager.postWeight(userWeight)  in class UtenteViewModel "
            )
            Log.d("LIBRA", "id paziente è " + id)
            if (it?.peso != null) {
                confirmation.setValue(Status.SUCCESS)
                Log.d("LIBRA", "    restApiManager.addGoal() : Success registering new goal")
                Log.d("LIBRA", "    it = " + it.toString() + "  --  classe: " + it.javaClass)
            } else {
                confirmation.setValue(Status.ERROR)
                Log.d("LIBRA", "    restApiManager.addGoal() : Error registering new goal")
            }
            Log.d("LIBRA", "  confirmationStatus in ViewModel: " + confirmation.value.toString())
        }
    }


    // MISURAZIONI PESO PER GRAFICO
    private var _pesoGrafico = MutableLiveData<List<Peso>>()
    var pesoGrafico : LiveData<List<Peso>> = _pesoGrafico

    fun getWeightFromUserData() : LiveData<List<Peso>>{
        _pesoGrafico = MutableLiveData<List<Peso>>().also{
            it.value = utenteCorrente.storico_pesi
        }
        pesoGrafico = _pesoGrafico
        return pesoGrafico
    }


    // OBIETTIVI
    private var _obiettiviStorico = MutableLiveData<List<Obiettivo>>()
    var obiettiviStorico : LiveData<List<Obiettivo>> = _obiettiviStorico

    fun getGoalsFromUserData() : LiveData<List<Obiettivo>>{
        _obiettiviStorico = MutableLiveData<List<Obiettivo>>().also{
            it.value = utenteCorrente.obiettivi
        }
        obiettiviStorico = _obiettiviStorico
        return obiettiviStorico
    }

}