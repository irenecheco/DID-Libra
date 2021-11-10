package it.polito.s279941.libra.utenteobiettivi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.api.RestApiManager

//https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
//https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// si occupa di recuperare i dati dalla fonte (DB esterno o cache interna)

class ObiettiviRepository (private val restApiManager: RestApiManager){

    fun getGoalsList(): MutableLiveData<List<Obiettivo>> {
        Log.d("LIBRAgoals","2.  class ObiettiviRepository, fun getGoalsList()")
        var _obiettiviStorico: List<Obiettivo>? = emptyList()

        val idPaziente = "6071aea342e7530e8c1947ed"  // TODO recuperare id paziente in questione

        restApiManager.getGoals(idPaziente) {
            Log.d("LIBRAgoals","3.  class ObiettiviViewModel, fun getGoals(), apiManager.getGoal()")
            if (it != null) {
                Log.d("LIBRAgoals", "4.  Success getGoals()")
                Log.d("LIBRAgoals", "5.  GOALS: " + it.toString())
                _obiettiviStorico = it
            } else {
                Log.d("LIBRA", "Error getGoals()")
            }
        }

        val _obiettiviStoricoLiveData = MutableLiveData<List<Obiettivo>>().also{
            it.value = _obiettiviStorico
        }

        return _obiettiviStoricoLiveData
    }

}