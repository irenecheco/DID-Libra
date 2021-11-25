package it.polito.s279941.libra.professionista

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.landing.LandingPageViewModel
import it.polito.s279941.libra.utils.LOG_TAG
import it.polito.s279941.libra.utils.Status

class ProfessionistaViewModel: ViewModel() {

    val restApiManager = RestApiManager()

    /** @AG **/
    // var che contiene l'oggetto utente (che ha fatto login/signin) passato dall'activity di login/signin
    var utenteCorrente : UtenteDataClass = UtenteDataClass()
    // funzione di test per verificare se effettivamente l'oggetto c'è
    fun initByUtenteDataClass_AG(userData: UtenteDataClass){
        Log.d(LOG_TAG, "initByUtenteDataClass_AG() tipologia utente: ${utenteCorrente.tipo} , email: ${utenteCorrente.email}  in ProfessionistaViewModel")
        Log.d(LOG_TAG, "initByUtenteDataClass_AG() Lista Pazienti: ${utenteCorrente.lista_pazienti}")
    }


    // LISTA PAZIENTI
    private var _pazientiLista = MutableLiveData<MutableList<Paziente>>()
    var pazientiLista : LiveData<MutableList<Paziente>> = _pazientiLista

    fun getIdPatientFromUserData() : LiveData<MutableList<Paziente>>{
        _pazientiLista = MutableLiveData<MutableList<Paziente>>().also{
            it.value = utenteCorrente.lista_pazienti as MutableList<Paziente>
        }
        pazientiLista = _pazientiLista
        return pazientiLista
    }


    // OBIETTIVI
    var confirmationAddGoal: MutableLiveData<Status> = MutableLiveData<Status>()
    fun addGoal(idPaziente: String, newUserGoal: Obiettivo) {
        Log.d("LIBRAgoals","start fun addGoal() in class ProfessionistaViewModel")

        confirmationAddGoal.setValue(Status.LOADING)
        restApiManager.addGoal(idPaziente,newUserGoal) {
            Log.d("LIBRAgoals","  start fun restApiManager.addGoal(userGoal) in class ProfessionistaViewModel ")
            if (it?.obiettivo != null) {
                confirmationAddGoal.setValue(Status.SUCCESS)
                Log.d("LIBRAgoals", "    restApiManager.addGoal() : Success registering new goal")
                Log.d("LIBRAgoals", "    it = " + it.toString() + "  --  classe: " + it.javaClass)
            } else {
                confirmationAddGoal.setValue(Status.ERROR)
                Log.d("LIBRAgoals", "    restApiManager.addGoal() : Error registering new goal")
            }
            Log.d("LIBRAgoals", "  confirmationStatus in ViewModel: " + confirmationAddGoal.value.toString())
        }
    }
}