package it.polito.s279941.libra.professionistadieta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfessionistaGiorniDietaPazienteViewModel: ViewModel() {

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
        _giornoInModifica = _giorni.get(idx)
        _giornoInModificaLiveData.value = _giornoInModifica
    }

}