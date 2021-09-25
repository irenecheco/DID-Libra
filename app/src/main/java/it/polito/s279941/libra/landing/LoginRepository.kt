package it.polito.s279941.libra.landing

import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.api.RestApiManager

//https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
//https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// si occupa di recuperare i dati dalla fonte (DB esterno o cache interna)

class LoginRepository (private val webService: RestApiManager){

    fun login(loginData: UtenteLoginData): MutableLiveData<UtenteDataClass> {
        var data = MutableLiveData<UtenteDataClass>()
        data = webService.login(loginData)
        return data
    }

}