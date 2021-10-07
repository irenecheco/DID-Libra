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

    fun getGoals(onResult: (List<Obiettivo>?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        // richiamo al metodo associato alla richiesta REST
        retrofit.getGoals().enqueue(
            object : Callback<List<Obiettivo>> {
                override fun onFailure(call: Call<List<Obiettivo>>, t: Throwable) {
                    Log.d("LIBRA", "  start onFailure()  in  retrofit.getGoal().enqueue  in  RestApiManager")
                    Log.d("LIBRA", "    throwable mess: " + t.message)
                    onResult(null)
                }
                override fun onResponse(call: Call<List<Obiettivo>>, response: Response<List<Obiettivo>>) {
                    Log.d("LIBRA", "  start onResponse()  in  retrofit.getGoal().enqueue  in  RestApiManager")
                    Log.d("LIBRA", "    status code: " + response.code())
                    val listOfGoals = response.body()
                    onResult(listOfGoals)
                }
            }
        )
    }

    fun addGoal(userGoal: Obiettivo, onResult: (Obiettivo?) -> Unit){
        Log.d("LIBRA","start fun addGoal() in class RestApiManager")
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        // richiamo al metodo associato alla richiesta REST
        val myCall = retrofit.addGoal(userGoal)
        myCall.enqueue( object : Callback<Obiettivo> {
            override fun onFailure(call: Call<Obiettivo>, t: Throwable) {
                Log.d("LIBRA", "  start onFailure()  in  retrofit.addGoal(userGoal).enqueue  in  RestApiManager")
                Log.d("LIBRA", "    throwable mess: " + t.message)
                onResult(null)
            }
            override fun onResponse(call: Call<Obiettivo>, response: Response<Obiettivo>) {
                Log.d("LIBRA", "  start onResponse()  in  retrofit.addGoal(userGoal).enqueue  in  RestApiManager")
                Log.d("LIBRA", "    status code: " + response.code())
                if(response?.body() != null)
                    Log.d("LIBRA", "    response.body= " + response.body().toString())
                val addedGoal = response.body()
                onResult(addedGoal)
            }
        }
        )
    }

}