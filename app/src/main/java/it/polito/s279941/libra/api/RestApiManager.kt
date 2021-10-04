package it.polito.s279941.libra.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.DataModel.Peso
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

class RestApiManager {

    fun login(utenteLoginData: UtenteLoginData): MutableLiveData<UtenteDataClass> {
        Log.d(LOG_TAG, "start fun  login()  in class RestApiManager") //--->DBG
        val utenteData = MutableLiveData<UtenteDataClass>()

        val apiService = ServiceBuilder.buildService(RestApi::class.java)

        apiService.login(utenteLoginData).enqueue(
            object : Callback<UtenteDataClass> {
                override fun onResponse(call: Call<UtenteDataClass>, response: Response<UtenteDataClass>) {
                    Log.d(LOG_TAG, "RestApiManager.onResponse() -> response.isSuccessful=" + response.isSuccessful) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.onResponse() -> response.code()=" + response.code()) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.onResponse() -> response.body()=" + response.body()) //--->DBG
                    utenteData.value = response.body()
                }

                override fun onFailure(call: Call<UtenteDataClass>, t: Throwable) {
                    Log.d(LOG_TAG, "RestApiManager.onFailure() -> throwable.message=" + t.message) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.onFailure() -> throwable.cause=" + t.cause) //--->DBG
                }
            }
        )
        return utenteData
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

    fun postWeight(userWeight: Peso, onResult: (Peso?) -> Unit){
        Log.d("LIBRA","start fun  postWeight()  in class RestApiManager")

        val retrofit = ServiceBuilder.buildService(RestApi::class.java)

        // qui richiamo il metodo associato alla richiesta REST
        val myCall = retrofit.postWeight(userWeight)
        myCall.enqueue( object : Callback<Peso> {
            override fun onFailure(call: Call<Peso>, t: Throwable) {
                Log.d("LIBRA", "  start onFailure()  in  retrofit.postWeight(userWeight).enqueue  in  RestApiManager")
                Log.d("LIBRA", "    throwable mess: " + t.message)
                onResult(null)
            }
            override fun onResponse(call: Call<Peso>, response: Response<Peso>) {
                Log.d("LIBRA", "  start onResponse()  in  retrofit.postWeight(userWeight).enqueue  in  RestApiManager")
                Log.d("LIBRA", "    status code: " + response.code())
                if(response?.body() != null)
                    Log.d("LIBRA", "Measurement added, response.body= " + response.body().toString())
                val postedWeight = response.body()
                onResult(postedWeight)
            }
        }
        )
    }

}
