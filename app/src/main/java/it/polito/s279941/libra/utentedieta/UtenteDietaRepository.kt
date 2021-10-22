package it.polito.s279941.libra.utentedieta

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.CommentoDietaPerUpdateDB
import it.polito.s279941.libra.DataModel.Dieta
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.api.RestApiManager

//https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
//https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// si occupa di recuperare i dati dalla fonte (DB esterno o cache interna)

class UtenteDietaRepository (private val restApiManager: RestApiManager){

    fun saveDieta(idPaziente: String, dieta: Dieta) {
        Log.d("aaaa","Salvataggio dieta")
        restApiManager.putDieta(idPaziente, dieta) {
            Log.d("aaaaa","3.  class ObiettiviViewModel, fun getGoals(), apiManager.getGoal()")
            if (it != null) {
                Log.d("aaaa", "4.  Success getGoals()")
                Log.d("aaaa", "5.  GOALS: " + it.toString())
                // _obiettiviStorico = it
            } else {
                Log.d("aaaaa", "Error saveDieta()")
            }
        }

        // val _obiettiviStoricoLiveData = MutableLiveData<List<Obiettivo>>().also{
        //     it.value = _obiettiviStorico
        // }

        // return _obiettiviStoricoLiveData
    }

    fun getPaziente(idPaziente: String, onResult: (UtenteDataClass?) -> Unit) //: MutableLiveData<UtenteDataClass>
    {
        Log.d("aaaa","get dieta:"+idPaziente)
        //var _paziente: UtenteDataClass? = UtenteDataClass()
        restApiManager.getPaziente(idPaziente) {
            Log.d("aaaaa","3.  class ObiettiviViewModel, fun getGoals(), apiManager.getGoal()")

            if (it != null) {
                Log.d("aaaa", "4.  Success getDieta()")
                Log.d("aaaa", "5.  GOALS: " + it.toString())
                 // _paziente = it
                onResult(it)
            } else {
                Log.d("aaaaa", "Error getDieta()")
            }
        }

        //return _pazienteLiveData
    }

    fun saveCommento(idPaziente: String, data: String, note: CommentoDietaPerUpdateDB) {
        Log.d("aaaa","Salvataggio dieta")
        restApiManager.postCommento(idPaziente, data, note) {
            Log.d("aaaaa","3.  class ObiettiviViewModel, fun getGoals(), apiManager.getGoal()")
           // if (it != null) {
                // Log.d("aaaa", "4.  Success getGoals()")
              //  Log.d("aaaa", "5.  GOALS: " + it.toString())
                // _obiettiviStorico = it
            //} else {
              //  Log.d("aaaaa", "Error saveDieta()")
            //}
        }
    }
}