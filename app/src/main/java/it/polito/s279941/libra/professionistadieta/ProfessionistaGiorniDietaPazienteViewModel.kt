package it.polito.s279941.libra.professionistadieta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Dieta
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utentedieta.PastoItem
import it.polito.s279941.libra.utentedieta.UtenteDietaRepository
import java.util.*

class ProfessionistaGiorniDietaPazienteViewModel: ViewModel() {

    val restApiManager = RestApiManager()
    val utenteDietaRepository = UtenteDietaRepository(restApiManager)


    //ìèò private var _paziente: UtenteDataClass = UtenteDataClass()
    //
    fun pazienteRicaricato(paz: UtenteDataClass){
        Log.d("aaaa",": " + paz)

        var _paziente = paz //  paziente.value!!


        if (_paziente.dieta==null) {

            Log.d("aaaa",": IL paziente non ha la dieta gli inserisco una dieta finta. ATTENZIONE: PUO CAPITARE ANCHE QUANDO NON VA BENE IL RECUPERO DEL PAZIENTE DAL DATABASE E CAPITA ANCHE AL PRIMO PASSAGGIO IN QUESTO PUNTO PERCHE' IL LIVE DATA VIENE RISOLTO IN UN GIRO SUCCESSIVO" + paziente)


            // Per ora mettiamo un utente mock
            //Log.d("aaaa","IdPaziente ${idPaziente}")
            _paziente = UtenteDataClass()
            _paziente.dieta = Dieta("2021-10-09",
                mutableListOf<GiornoDieta>(
                    GiornoDieta(0,"briosche","biscotto","spaghetti","yogurt","mela") ,
                    GiornoDieta(1,"marmellata","tost","ragu","uva","pizza"),
                    GiornoDieta(2,"nutella","caffe","lasagne","torta","frittura di pesce")
                )
            )
            // Fine mock
        }

        val d= (_paziente.dieta!!.data_inizio?:"2021-10-12").split("T")[0].split("-")
        val cal = Calendar.getInstance()
        cal.set(d[0].toInt(),d[1].toInt()-1,d[2].toInt())
        setGiornoInizioDieta(cal.timeInMillis)

        _giorni = _paziente.dieta!!.giorni
        _giorniLiveData.value = _giorni

    }

    //private var  _paziente: UtenteDataClass =  UtenteDataClass()
    private var _PazienteLiveData = MutableLiveData<UtenteDataClass>().also{it.value = UtenteDataClass()}
    var paziente : LiveData<UtenteDataClass> = _PazienteLiveData
    fun getPazienteFromRepository(idPaziente: String) {//} : LiveData<UtenteDataClass>{
         utenteDietaRepository.getPaziente(idPaziente) {
             //paziente.value = _PazienteLiveData.value
             //_paziente = it // _PazienteLiveData.value ?: UtenteDataClass()
             _PazienteLiveData.value = it
             pazienteRicaricato(it?:UtenteDataClass() )
         }


    }
    /**
     * Quando viene letto l'utente occorre che qualcuno richiami questo metodo
     *
     */
    fun setPaziente(idPaziente: String){
        // TODO: Occorre leggere il paziente dal Database
        // GET http://localhost:3000/api/users/{idUtente} // 6071aea342e7530e8c1947ed
        getPazienteFromRepository(idPaziente)
    }


    private var _giornoInizioDieta: Long = System.currentTimeMillis()
    private val _giornoInizioDietaLiveData = MutableLiveData<Long>().also{it.value = _giornoInizioDieta}
    val giornoInizioDietaLiveData : LiveData<Long> = _giornoInizioDietaLiveData
    fun setGiornoInizioDieta(giorno: Long) {
        _giornoInizioDieta = giorno
        _giornoInizioDietaLiveData.value = _giornoInizioDieta

        var d=Date(giorno)

        //ìèò_paziente.dieta!!.data_inizio = (d.year+1900).toString() +"-"+ (if (d.month<9) "0" else "") + (d.month+1)+"-" + (if (d.date>9) "" else "0")+ d.date
        paziente.value?.dieta!!.data_inizio = (d.year+1900).toString() +"-"+ (if (d.month<9) "0" else "") + (d.month+1)+"-" + (if (d.date>9) "" else "0")+ d.date
        // Data sarà nel formato YYYY-MM-GG


        //saveDietaPaziente()
    }
    fun getGiornoInizioDieta(): Long {
        return _giornoInizioDieta
    }


    private var _giorni= mutableListOf<GiornoDieta>()
    private val _giorniLiveData = MutableLiveData<MutableList<GiornoDieta>>().also{
        it.value = _giorni
    }
    val giorniLiveData : LiveData<MutableList<GiornoDieta>> = _giorniLiveData
    fun getGiorniDietaPaziente(): MutableList<GiornoDieta> {
        return _giorni
    }
    fun addGiorno() {
        //ìèò val nuovoGiorno = GiornoDieta(_paziente.dieta?.giorni!!.size,"","","","","")
        val nuovoGiorno = GiornoDieta(paziente.value!!.dieta?.giorni!!.size,"","","","","")
        _giorni.add(nuovoGiorno)
        _giorniLiveData.value = _giorni
    }


    // riferimento al corrente giorno in editing
    private var _giornoInModifica: GiornoDieta? = null
    private val _giornoInModificaLiveData = MutableLiveData<GiornoDieta>().also{
        it.value = _giornoInModifica
    }
    val giornoInModificaLiveData : LiveData<GiornoDieta> = _giornoInModificaLiveData
    fun setGiornoInModifica(idx: Int) {
        _giornoInModifica = _giorni[idx]
        _giornoInModificaLiveData.value = _giornoInModifica
    }
    fun updateDatiGiornoInModifica(colazione: String, spuntino : String, pranzo : String, merenda: String, cena: String ) {

        /*_giornoInModifica?._pastiDelGiorno?.get(0)?.descrizione = colazione
        _giornoInModifica?._pastiDelGiorno?.get(1)?.descrizione = spuntino
        _giornoInModifica?._pastiDelGiorno?.get(2)?.descrizione = pranzo
        _giornoInModifica?._pastiDelGiorno?.get(3)?.descrizione = merenda
        _giornoInModifica?._pastiDelGiorno?.get(4)?.descrizione = cena
*/

        Log.d("aaaa","colazione:"+_giornoInModifica?.colazione+"=>"+colazione)
        _giornoInModifica?.colazione = colazione
        _giornoInModifica?.spuntinoMattina = spuntino
        _giornoInModifica?.pranzo = pranzo
        _giornoInModifica?.spuntinoPomeriggio = merenda
        _giornoInModifica?.cena = cena

        _giornoInModificaLiveData.value = _giornoInModifica
        //saveDietaPaziente()
    }

    fun saveDietaPaziente(){
        // PUT http://localhost:3000/api/nut/set-dieta/idpazienteeeeeee
        // content-type: application/json
    // TODO: Salvare la dieta per il dato paziente
        utenteDietaRepository.saveDieta(paziente.value!!._id,paziente.value!!.dieta!!)
        // ìèò _paziente.dieta
       // paziente.value!!.dieta

    }

}