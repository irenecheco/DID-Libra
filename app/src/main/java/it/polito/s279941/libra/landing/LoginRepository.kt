package it.polito.s279941.libra.landing

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utils.LOG_TAG

//https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
//https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// si occupa di recuperare i dati dalla fonte (DB esterno o cache interna)
// se non c'è rete (il dato dal webervice non arriva) deve prendere
// dal db interno (room library) (da implementare)
// per ora assumiamo che la rete funzioni.
// https://developer.android.com/jetpack/guide#overview
// è questa classe che deve decidere se recuperare i dati dal db esterno o locale
class LoginRepository (private val webService: RestApiManager){
    fun login(loginData: UtenteLoginData): MutableLiveData<UtenteDataClass> {
        var data = MutableLiveData<UtenteDataClass>()
        data = webService.login(loginData)
        if (data == null) {
            Log.d(LOG_TAG, "data from RestAPI=null  in LoginRepository")
        }
        return data
    }
}