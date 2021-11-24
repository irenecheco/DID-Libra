package it.polito.s279941.libra.professionista

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.professionistapazienti.PazientiItem
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


    // LISTA PAZIENTI --> recupera la lista degli id dei pazienti del nutrizionista
    private var _pazientiLista = MutableLiveData<List<Paziente>>()
    var pazientiLista : LiveData<List<Paziente>> = _pazientiLista

    fun getIdPatientFromUserData() : LiveData<List<Paziente>>{
        _pazientiLista = MutableLiveData<List<Paziente>>().also{
            it.value = utenteCorrente.lista_pazienti
        }
        pazientiLista = _pazientiLista
        return pazientiLista
    }


    // OBIETTIVI
    var confirmationAddGoal: MutableLiveData<Status> = MutableLiveData<Status>()
    fun addGoal(newUserGoal: Obiettivo) {
        Log.d("LIBRAgoals","start fun addGoal() in class ProfessionistaViewModel")
        val idPaziente = "617ea2a7b5bee74a7064f702"     // utente q@q.com
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