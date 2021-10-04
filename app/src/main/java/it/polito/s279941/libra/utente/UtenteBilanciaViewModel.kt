package it.polito.s279941.libra.utente

import android.util.Log
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.DataModel.Peso
import java.util.*

class UtenteBilanciaViewModel : ViewModel() {

    // TODO: salva peso tra dati utente in locale, al momento aggiorna solo il server

    var userWeight = Peso(
        data = null,
        peso = null
    )

    fun postWeight(new_date: Date, new_weight: Double){

        userWeight.data = new_date
        userWeight.peso = new_weight

        Log.d("LIBRA", "start fun postWeight() in class UtenteBilanciaViewModel")
        Log.d("LIBRA", "attuale valore di userWeight: data " + userWeight.data + ", peso " + userWeight.peso )

        val restApiManager = RestApiManager()
        restApiManager.postWeight(userWeight) {
            Log.d(
                "LIBRA",
                "start fun restApiManager.postWeight(userWeight)  in class UtenteBilanciaViewModel "
            )
        }
    }
}