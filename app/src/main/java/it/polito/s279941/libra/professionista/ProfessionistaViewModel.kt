package it.polito.s279941.libra.professionista

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.professionistapazienti.PazientiItem
import it.polito.s279941.libra.utentedieta.PastoItem
import it.polito.s279941.libra.utenteobiettivi.ObiettiviItem

class ProfessionistaViewModel: ViewModel() {

    private val _pazienti = mutableListOf(
            PazientiItem("url foto", "Alice Rossi", System.currentTimeMillis()-100000000),
            PazientiItem("url foto", "Mario Bianchi", System.currentTimeMillis()-1000000000),
            PazientiItem("url foto", "Giuseppe Verdi", System.currentTimeMillis()-3000000000),
            PazientiItem("url foto", "Alessandro Neri", System.currentTimeMillis()-7000000000)
    )

    private val _pazientiLiveData = MutableLiveData<List<PazientiItem>>().also{
        it.value = _pazienti
    }

    val pazientiLiveData : LiveData<List<PazientiItem>> = _pazientiLiveData

    fun addPazienti(foto: String, nome: String, ultimoControllo_data: Long) {
        val nuovoPaziente = PazientiItem(foto, nome, ultimoControllo_data)
        _pazienti.add(nuovoPaziente)
        _pazientiLiveData.value = _pazienti
    }
}