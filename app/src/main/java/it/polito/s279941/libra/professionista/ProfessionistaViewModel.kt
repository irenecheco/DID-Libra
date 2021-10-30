package it.polito.s279941.libra.professionista

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.professionistapazienti.PazientiItem
import it.polito.s279941.libra.utils.LOG_TAG

class ProfessionistaViewModel: ViewModel() {

    /** @AG **/
    // var che contiene l'oggetto utente (che ha fatto login/signin) passato dall'activity di login/signin
    var utenteCorrente : UtenteDataClass = UtenteDataClass()
    // funzione di test per verificare se effettivamente l'oggetto c'è
    fun initByUtenteDataClass_AG(userData: UtenteDataClass){
        Log.d(LOG_TAG, "initByUtenteDataClass_AG() tipologia utente: ${utenteCorrente.tipo} , email: ${utenteCorrente.email}  in ProfessionistaViewModel")
    }


    private val _pazienti = mutableListOf(
            PazientiItem("url foto", "Alice Rossi", System.currentTimeMillis()-100000000),
            PazientiItem("url foto", "Mario Bianchi", System.currentTimeMillis()-1000000000),
            PazientiItem("url foto", "Giuseppe Verdi", System.currentTimeMillis()-3000000000),
            PazientiItem("url foto", "Alessandro Neri", System.currentTimeMillis()-7000000000)
    )

    private val _pazientiLiveData = MutableLiveData<MutableList<PazientiItem>>().also{
        it.value = _pazienti
    }

    val pazientiLiveData : LiveData<MutableList<PazientiItem>> = _pazientiLiveData

    fun addPazienti(foto: String, nome: String, ultimoControllo_data: Long) {
        val nuovoPaziente = PazientiItem(foto, nome, ultimoControllo_data)
        _pazienti.add(nuovoPaziente)
        _pazientiLiveData.value = _pazienti
    }
}