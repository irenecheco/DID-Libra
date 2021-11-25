package it.polito.s279941.libra.professionista

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.PazientiItem
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
    // recupera la lista degli id dei pazienti del nutrizionista
    private var _pazientiLista = MutableLiveData<MutableList<Paziente>>()
    var pazientiLista : LiveData<MutableList<Paziente>> = _pazientiLista

    fun getIdPatientFromUserData() : LiveData<MutableList<Paziente>>{
        _pazientiLista = MutableLiveData<MutableList<Paziente>>().also{
            it.value = utenteCorrente.lista_pazienti as MutableList<Paziente>
        }
        pazientiLista = _pazientiLista
        return pazientiLista
    }

    // lista per convertire Paziente in PazientItem
    var _listaPazientiItem = mutableListOf<PazientiItem>()
    private val _listaPazientiItemLiveData = MutableLiveData<MutableList<PazientiItem>>().also{
        it.value = _listaPazientiItem
    }
    val listaPazientiItemLiveData : LiveData<MutableList<PazientiItem>> = _listaPazientiItemLiveData


    // OBIETTIVI
    var confirmationAddGoal: MutableLiveData<Status> = MutableLiveData<Status>()
    fun addGoal(newUserGoal: Obiettivo) {
        Log.d("LIBRAgoals","start fun addGoal() in class ProfessionistaViewModel")
        val idPaziente = "619e274d328e957ddd522ce2"     // utente q@q.com
        //val idPaziente = utenteCorrente._id  -->  è null
        //val idPaziente TODO con getPaziente per trovare id paziente a cui assegnare obiettivo

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