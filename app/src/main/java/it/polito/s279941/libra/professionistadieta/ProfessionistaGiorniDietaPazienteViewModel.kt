package it.polito.s279941.libra.professionistadieta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.utentedieta.PastoItem

class ProfessionistaGiorniDietaPazienteViewModel: ViewModel() {



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


    private val _giorni= mutableListOf<GiornoItem>()

    private val _giorniLiveData = MutableLiveData<MutableList<GiornoItem>>().also{
        it.value = _giorni
    }

    val giorniLiveData : LiveData<MutableList<GiornoItem>> = _giorniLiveData

    fun addGiorno() {
        val nuovoGiorno = GiornoItem()
        _giorni.add(nuovoGiorno)
        _giorniLiveData.value = _giorni
    }


    // riferimento al corrente giorno in editing
    private var _giornoInModifica: GiornoItem? = null
    private val _giornoInModificaLiveData = MutableLiveData<GiornoItem>().also{
        it.value = _giornoInModifica
    }
    val giornoInModificaLiveData : LiveData<GiornoItem> = _giornoInModificaLiveData
    fun setGiornoInModifica(idx: Int) {
        _giornoInModifica = _giorni[idx]
        _giornoInModificaLiveData.value = _giornoInModifica
    }
    fun updateDatiGiornoInModifica(colazione: String, spuntino : String, pranzo : String, merenda: String, cena: String ) {
        Log.d("aaaa","colazione:"+_giornoInModifica?._pastiDelGiorno?.get(0)?.descrizione+"=>"+colazione)
        _giornoInModifica?._pastiDelGiorno?.get(0)?.descrizione = colazione
        _giornoInModifica?._pastiDelGiorno?.get(1)?.descrizione = spuntino
        _giornoInModifica?._pastiDelGiorno?.get(2)?.descrizione = pranzo
        _giornoInModifica?._pastiDelGiorno?.get(3)?.descrizione = merenda
        _giornoInModifica?._pastiDelGiorno?.get(4)?.descrizione = cena

        _giornoInModificaLiveData.value = _giornoInModifica
        // TODO_ Salvare i dati sul DB
    }

}