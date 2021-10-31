package it.polito.s279941.libra.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.*
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
                    Log.d(LOG_TAG, "RestApiManager.login.onResponse() -> response.isSuccessful=" + response.isSuccessful) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.login.onResponse() -> response.code()=" + response.code()) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.login.onResponse() -> response.body()=" + response.body()) //--->DBG
                    when (response.code()){
                        200 -> {
                            Log.d(LOG_TAG, "when( response.code() == 200)") //--->DBG
                            Log.d(LOG_TAG, "  right credential, login allowed") //--->DBG
                            utenteData.value = response.body()
                        }
                        401 -> {
                            Log.d(LOG_TAG, "when( response.code() == 401)") //--->DBG
                            Log.d(LOG_TAG, "  wrong password") //--->DBG
                            // questa istr. serve a terminare correttamente la chiamata retrofit, che altrimenti resta "appesa"
                            utenteData.value = response.body()
                            /* questa serve ad aggiornare il LiveData con il codice di errore passato nel campo 'tipo',
                            * in modo da poter dare un certo feedback all''utente (ved. LoginPageFragment e SigninPageFragment)
                            * Non è proprio bellissimo, ma non ho trovato altre soluzioni che non stravolgessero e/o complicassero
                            * il codice.
                            * In pratica in questo modo il LiveData viene "aggiornato" 2 volte:
                            *  - la prima volta vale 'null'
                            *  - la seconda volta carico il livedata con una istanza di utenteDataClass a cui assegno al
                            * campo tipo il codice di errore
                             */
                            utenteData.value = UtenteDataClass(tipo=response.code().toString())
                        }
                        404 -> {
                            Log.d(LOG_TAG, "when( response.code() == 404)") //--->DBG
                            Log.d(LOG_TAG, "  user not exist") //--->DBG
                            // questa istr. serve a terminare correttamente la chiamata retrofit, che altrimenti resta "appesa"
                            utenteData.value = response.body()
                            /* questa serve ad aggiornare il LiveData con il codice di errore passato nel campo 'tipo',
                            * in modo da poter dare un certo feedback all''utente (ved. LoginPageFragment e SigninPageFragment)
                            * Non è proprio bellissimo, ma non ho trovato altre soluzioni che non stravolgessero e/o complicassero
                            * il codice.
                            * In pratica in questo modo il LiveData viene "aggiornato" 2 volte:
                            *  - la prima volta vale 'null'
                            *  - la seconda volta carico il livedata con una istanza di utenteDataClass a cui assegno al
                            * campo tipo il codice di errore
                             */
                            utenteData.value = UtenteDataClass(tipo=response.code().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<UtenteDataClass>, t: Throwable) {
                    Log.d(LOG_TAG, "RestApiManager.login.onFailure() -> throwable.message=" + t.message) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.login.onFailure() -> throwable.cause=" + t.cause) //--->DBG
                }
            }
        )
        return utenteData
    }




    fun signin(utenteSigninData: UtenteSigninData): MutableLiveData<UtenteDataClass> {
        Log.d(LOG_TAG, "start fun  signin()  in class RestApiManager") //--->DBG
        val utenteData = MutableLiveData<UtenteDataClass>()

        val apiService = ServiceBuilder.buildService(RestApi::class.java)

        apiService.signin(utenteSigninData).enqueue(
            object : Callback<UtenteDataClass> {
                override fun onResponse(call: Call<UtenteDataClass>, response: Response<UtenteDataClass>) {
                    Log.d(LOG_TAG, "RestApiManager.signin.onResponse() -> response.isSuccessful=" + response.isSuccessful) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.signin.onResponse() -> response.code()=" + response.code()) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.signin.onResponse() -> response.body()=" + response.body()) //--->DBG
                    when (response.code()){
                        201 -> {
                            Log.d(LOG_TAG, "when( response.code() == 201)") //--->DBG
                            Log.d(LOG_TAG, "  user created, signin allowed") //--->DBG
                            utenteData.value = response.body()
                        }
                        409 -> {
                            Log.d(LOG_TAG, "when( response.code() == 409)") //--->DBG
                            Log.d(LOG_TAG, "  user already exists") //--->DBG
                            // v. login()
                            utenteData.value = response.body()
                            utenteData.value = UtenteDataClass(tipo=response.code().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<UtenteDataClass>, t: Throwable) {
                    Log.d(LOG_TAG, "RestApiManager.signin.onFailure() -> throwable.message=" + t.message) //--->DBG
                    Log.d(LOG_TAG, "RestApiManager.signin.onFailure() -> throwable.cause=" + t.cause) //--->DBG
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
                    Log.d("LIBRAgoals", "  start onFailure()  in  retrofit.getGoals().enqueue  in  RestApiManager")
                    Log.d("LIBRAgoals", "    throwable mess: " + t.message)
                    onResult(null)
                }
                override fun onResponse(call: Call<List<Obiettivo>>, response: Response<List<Obiettivo>>) {
                    Log.d("LIBRAgoals", "  start onResponse()  in  retrofit.getGoals().enqueue  in  RestApiManager")
                    Log.d("LIBRAgoals", "    status code: " + response.code())
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
                Log.d("LIBRAgoals", "  start onFailure()  in  retrofit.addGoal(userGoal).enqueue  in  RestApiManager")
                Log.d("LIBRAgoals", "    throwable mess: " + t.message)
                onResult(null)
            }
            override fun onResponse(call: Call<Obiettivo>, response: Response<Obiettivo>) {
                Log.d("LIBRAgoals", "  start onResponse()  in  retrofit.addGoal(userGoal).enqueue  in  RestApiManager")
                Log.d("LIBRAgoals", "    status code: " + response.code())
                if(response.body() != null)
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







    // =================================================================================
    // Metodi per gestione della dieta utente salvata dal professionista e delle modifiche alla dieta effettuate dal paziente
    // aggiunti da Sofia in corrispondenza di quelli aggiunti in RestApi
    // =================================================================================
    fun getPaziente(idPaziente: String, onResult: (UtenteDataClass?) -> Unit){
        Log.d("aaaa","getDieta in class RestApiManager")
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        val myCall = retrofit.getPaziente(idPaziente) //a cosa corrisponde id paziente? quello del db?
        myCall.enqueue( object : Callback<UtenteDataClass> { //a cosa corrisponde utente data class?
            override fun onFailure(call: Call<UtenteDataClass>, t: Throwable) {
                Log.d("aaaa", "  start onFailure()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    throwable mess: " + t.message)
                onResult(null)
            }
            override fun onResponse(call: Call<UtenteDataClass>, response: Response<UtenteDataClass>) {
                Log.d("aaaa", "  start onResponse()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    status code: " + response.code())
                if(response.body() != null)
                    Log.d("aaaa", "    response.body= " + response.body().toString())
                val getDietaResponse = response.body() //quindi get dieta response è la variabile che qui su kotlin conterrà il dato del db
                onResult(getDietaResponse)
            }
        }
        )
    }
    /**
     * Salvataggio della dieta da parte del dietologo
     */
    fun putDieta(idPaziente: String, dieta: Dieta, onResult: (Dieta?) -> Unit){
        Log.d("aaaa","putDieta in class RestApiManager")
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        val myCall = retrofit.putDieta(idPaziente,dieta)
        myCall.enqueue( object : Callback<Dieta> {
            override fun onFailure(call: Call<Dieta>, t: Throwable) {
                Log.d("aaaa", "  start onFailure()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    throwable mess: " + t.message)
                onResult(null)
            }
            override fun onResponse(call: Call<Dieta>, response: Response<Dieta>) {
                Log.d("aaaa", "  start onResponse()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    status code: " + response.code())
                if(response.body() != null)
                    Log.d("aaaa", "    response.body= " + response.body().toString())
                val addDietaResponse = response.body()
                onResult(null)
            }
        }
        )
    }

    fun postCommento(idPaziente: String, data: String, note: CommentoDietaPerUpdateDB, onResult: () -> Unit){
        Log.d("aaaa","putDieta in class RestApiManager")
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        val myCall = retrofit.postCommento(idPaziente, data, note)
        myCall.enqueue( object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("aaaa", "  start onFailure()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    throwable mess: " + t.message)
                onResult()
            }
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("aaaa", "  start onResponse()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    status code: " + response.code())
                if(response.body() != null)
                    Log.d("aaaa", "    response.body= " + response.body().toString())
                val addDietaResponse = response.body()
                onResult()
            }
        }
        )
    }

    fun postCheckPasto(idPaziente: String, data: String, nomePasto: String, checkPasto: CheckPastoPerUpdateDB, onResult: () -> Unit){
        Log.d("aaaa","putDieta in class RestApiManager")
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        val myCall = retrofit.postCheckPasto(idPaziente, data, nomePasto, checkPasto)
        myCall.enqueue( object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("aaaa", "  start onFailure()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    throwable mess: " + t.message)
                onResult()
            }
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("aaaa", "  start onResponse()  in  retrofit.putDieta(userGoal).enqueue  in  RestApiManager")
                Log.d("aaaa", "    status code: " + response.code())
                if(response.body() != null)
                    Log.d("aaaa", "    response.body= " + response.body().toString())
                val addDietaResponse = response.body()
                onResult()
            }
        }
        )
    }

}
