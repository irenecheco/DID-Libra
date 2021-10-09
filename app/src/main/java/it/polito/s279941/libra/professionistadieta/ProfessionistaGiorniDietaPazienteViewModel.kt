package it.polito.s279941.libra.professionistadieta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Dieta
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.utentedieta.PastoItem
import java.util.*

class ProfessionistaGiorniDietaPazienteViewModel: ViewModel() {

    private var _paziente: UtenteDataClass = UtenteDataClass()

    /**
     * Quando viene letto l'utente occorre che qualcuno richiami questo metodo
     *
     */
    fun setPaziente(idPaziente: String){
        // TODO: Occorre leggere il paziente dal Database
        // GET http://localhost:3000/api/users/{idUtente} // 6071aea342e7530e8c1947ed
        // Per ora mettiamo un utente mock
        Log.d("aaaa","IdPaziente ${idPaziente}")
        _paziente = UtenteDataClass()
        _paziente.dieta = Dieta("2021-10-09",
            mutableListOf<GiornoDieta>(
                GiornoDieta(0,"briosche","biscotto","spaghetti","yogurt","mela") ,
                GiornoDieta(1,"marmellata","tost","ragu","uva","pizza"),
                GiornoDieta(2,"nutella","caffe","lasagne","torta","frittura di pesce")
            )
        )
        // Fine mock


        val d= _paziente.dieta!!.data_inizio.split("-")
        val cal = Calendar.getInstance()
        cal.set(d[0].toInt(),d[1].toInt(),d[2].toInt())
        setGiornoInizioDieta(cal.timeInMillis)

        _giorni = _paziente.dieta!!.giorni
        _giorniLiveData.value = _giorni

    }


    private var _giornoInizioDieta: Long = System.currentTimeMillis()
    private val _giornoInizioDietaLiveData = MutableLiveData<Long>().also{it.value = _giornoInizioDieta}
    val giornoInizioDietaLiveData : LiveData<Long> = _giornoInizioDietaLiveData
    fun setGiornoInizioDieta(giorno: Long) {
        _giornoInizioDieta = giorno
        _giornoInizioDietaLiveData.value = _giornoInizioDieta
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
        val nuovoGiorno = GiornoDieta(_paziente.dieta?.giorni!!.size,"","","","","")
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
        // TODO: Salvare i dati sul DB
    }

}