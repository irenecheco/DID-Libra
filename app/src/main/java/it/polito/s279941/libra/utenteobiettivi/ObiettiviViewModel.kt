package it.polito.s279941.libra.utenteobiettivi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.api.RestApiManager
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

//https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a

// In general, you’ll make a ViewModel class FOR EACH screen in your app
// Il vieModel gestisce i dati, invoca direttamente le chiamate REST o può delegarle al Repository

class ObiettiviViewModel : ViewModel() {

    fun getGoals(): LiveData<List<Obiettivo>> {
        Log.d("LIBRA","class ObiettiviViewModel, fun getGoals()")
        var _obiettiviStorico: List<Obiettivo>? = emptyList()
        val restApiManager = RestApiManager()

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
    }

    //private val _obiettiviStorico: MutableList<Obiettivo> = mutableListOf<Obiettivo>()
    //private val _obiettiviStoricoLiveData = MutableLiveData<List<Obiettivo>>().also{it.value = _obiettiviStorico}
    //val obiettiviStoricoLiveData : LiveData<List<Obiettivo>> = _obiettiviStoricoLiveData
}