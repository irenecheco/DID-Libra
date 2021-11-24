package it.polito.s279941.libra.landing

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteSigninData
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utils.LOG_TAG

//https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
//https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// si occupa di caricare i dati sulla fonte (DB esterno o cache interna)
// se non c'è rete (il dato dal webervice non arriva) deve prendere
// dal db interno (room library) (da implementare)
// per ora assumiamo che la rete funzioni.
// https://developer.android.com/jetpack/guide#overview
// è questa classe che deve decidere se recuperare i dati dal db esterno o locale

class SigninRepository (private val webService: RestApiManager){

    fun signin(signinData: UtenteSigninData): MutableLiveData<UtenteDataClass> {
        var data = MutableLiveData<UtenteDataClass>()
        data = webService.signin(signinData)
        if (data == null) {
            Log.d(LOG_TAG, "data from RestAPI=null  in LoginRepository")
        }
        return data
    }

}