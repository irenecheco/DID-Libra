package it.polito.s279941.libra.api

import android.util.Log
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.utils.LOG_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a

Finally, we implement the actual Service which we will directly invoke.
We call it service but don’t be confused with Android service.
If you feel you can rename the class as your own like with RestApiManager or ApiManager etc
*/

class `RestApiManager_pre-LiveData` {

    fun login(utenteLoginData: UtenteLoginData) {
        Log.d(LOG_TAG, "start fun  login()  in class RestApiManager")
        val apiService = ServiceBuilder.buildService(RestApi::class.java)
        apiService.login(utenteLoginData).enqueue(
            object : Callback<UtenteDataClass>{
                override fun onResponse(call: Call<UtenteDataClass>, response: Response<UtenteDataClass>) {
                    Log.d(LOG_TAG, "onResponse -> response.isSuccessful=" + response.isSuccessful)
                    Log.d(LOG_TAG, "onResponse -> response.code()=" + response.code())
                    Log.d(LOG_TAG, "onResponse -> response.body()=" + response.body())
                }

                override fun onFailure(call: Call<UtenteDataClass>, t: Throwable) {
                    Log.d(LOG_TAG, "onFailure -> throwable.message=" + t.message)
                    Log.d(LOG_TAG, "onFailure -> throwable.cause=" + t.cause)
                }
            }
        )
    }

}
