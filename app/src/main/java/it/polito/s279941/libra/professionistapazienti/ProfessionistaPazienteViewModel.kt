package it.polito.s279941.libra.professionistapazienti

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.*
import it.polito.s279941.libra.api.RestApiManager

class ProfessionistaPazienteViewModel: ViewModel() {
    val restApiManager = RestApiManager()

    /** @AG **/
    // var che contiene l'oggetto utente (che ha fatto login/signin) passato dall'activity di login/signin
    var pazienteCorrente : UtenteDataClass = UtenteDataClass()


    // usata per check che dati paziente presenti e che sia un paziente
    fun getTipologiaUtente(): String {
        // se .tipo è null assegno valore 'NETERR' come defalut
        //Log.d(LOG_TAG, "1 getTipologiaUtente()=${utenteCorrente.value?.tipo}  in LandingPageViewModel") //--->DBG
        val t = pazienteCorrente.tipo ?: "NETERR"
        //Log.d(LOG_TAG, "  2 getTipologiaUtente()=${utenteCorrente.value?.tipo}  in LandingPageViewModel")
        return t
    }

    // MISURAZIONI PESO PER GRAFICO
    private var _pesoGrafico = MutableLiveData<List<Peso>>()
    var pesoGrafico : LiveData<List<Peso>> = _pesoGrafico

    fun getWeightFromUserData() : LiveData<List<Peso>> {
        _pesoGrafico = MutableLiveData<List<Peso>>().also{
            it.value = pazienteCorrente.storico_pesi
        }
        pesoGrafico = _pesoGrafico
        return pesoGrafico
    }

    // OBIETTIVI
    private var _obiettiviStorico = MutableLiveData<List<Obiettivo>>()
    var obiettiviStorico : LiveData<List<Obiettivo>> = _obiettiviStorico

    fun getGoalsFromUserData() : LiveData<List<Obiettivo>> {
        _obiettiviStorico = MutableLiveData<List<Obiettivo>>().also{
            it.value = pazienteCorrente.obiettivi
        }
        obiettiviStorico = _obiettiviStorico
        return obiettiviStorico
    }
}