package it.polito.s279941.libra.utenteobiettivi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.landing.LoginRepository
import it.polito.s279941.libra.utils.LOG_TAG
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

//https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a

// In general, you’ll make a ViewModel class FOR EACH screen in your app
// Il vieModel gestisce i dati, invoca direttamente le chiamate REST o può delegarle al Repository

class ObiettiviViewModel : ViewModel() {

    val restApiManager = RestApiManager()
    val goalsRepository = ObiettiviRepository(restApiManager)

    private var _obiettiviStorico = MutableLiveData<List<Obiettivo>>()
    var obiettiviStorico : LiveData<List<Obiettivo>> = _obiettiviStorico

    fun getGoalsRepository() : LiveData<List<Obiettivo>>{
        _obiettiviStorico = goalsRepository.getGoalsList()
        obiettiviStorico = _obiettiviStorico

        Log.d("LIBRAgoals","6.  getGoalsRepository: " + obiettiviStorico)
        return obiettiviStorico
    }

    /*fun getGoals(): LiveData<List<Obiettivo>> {
        Log.d("LIBRA","class ObiettiviViewModel, fun getGoals()")
        var _obiettiviStorico: List<Obiettivo>? = emptyList()

        restApiManager.getGoals() {
            Log.d("LIBRA","  class ObiettiviViewModel, fun getGoals(), apiManager.getGoal()")
            if (it != null) {
                Log.d("LIBRA", "Success getGoals()")
                Log.d("LIBRA", "GOALS: " + it.toString())
                _obiettiviStorico = it
            } else {
                Log.d("LIBRA", "Error getGoals()")
            }
        }

        val _obiettiviStoricoLiveData = MutableLiveData<List<Obiettivo>>().also{
            it.value = _obiettiviStorico
        }

        val obiettiviStoricoLiveData : LiveData<List<Obiettivo>> = _obiettiviStoricoLiveData
        return obiettiviStoricoLiveData
    }*/

    fun addGoal(newUserGoal: Obiettivo) {
        Log.d("LIBRAgoals","start fun addGoal() in class ObiettiviViewModel")
        //val userGoal = Obiettivo(data_obiettivo = Date(), obiettivo = "obiettivo n")
        val userGoal = newUserGoal

        restApiManager.addGoal(userGoal) {
            Log.d("LIBRAgoals","  start fun restApiManager.addGoal(userGoal) in class ObiettiviViewModel ")
            if (it?.obiettivo != null) {
                Log.d("LIBRAgoals", "    restApiManager.addGoal() : Success registering new goal")
                Log.d("LIBRAgoals", "    it = " + it.toString() + "  --  classe: " + it.javaClass)
            } else {
                Log.d("LIBRAgoals", "    restApiManager.addGoal() : Error registering new goal")
            }
        }
    }

    //private val _obiettiviStorico: MutableList<Obiettivo> = mutableListOf<Obiettivo>()
    //private val _obiettiviStoricoLiveData = MutableLiveData<List<Obiettivo>>().also{it.value = _obiettiviStorico}
    //val obiettiviStoricoLiveData : LiveData<List<Obiettivo>> = _obiettiviStoricoLiveData
}