package it.polito.s279941.libra.api

import androidx.lifecycle.MutableLiveData
import it.polito.s279941.libra.DataModel.*
import retrofit2.Call
import retrofit2.http.*

/*
https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a

Retrofit models REST endpoints as interfaces.
In RestApi interface, we declare all the REST request methods with individual paths
for the endpoint. Although example is shown for a typical POST call, we can use
any REST methods on our own i.e. POST, GET, PUT, DELETE etc.
*/

interface RestApi {

    // http://BACKEND_URL:3000/api/auth/login (POST con json dei dati utente)
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(@Body utenteLoginData: UtenteLoginData) : Call<UtenteDataClass>


    /*@GET("users")
    fun getUsers2() : Observable<List<UtenteDataClass>>

    // id utente preso a caso dal server per vedere se funziona
    // poi ci sarà da creare una variabile che indica l'utente che sta usando l'app da incatenare al resto dell'indirizzo*/
    @GET("users/goals/6071aea342e7530e8c1947ed")
    fun getGoals(): Call<List<Obiettivo>>

    @POST("nut/add-goal/6071aea342e7530e8c1947ed")
    fun addGoal(@Body userData: Obiettivo): Call<Obiettivo>

    @POST("users/add-measurement/6071aea342e7530e8c1947ed") // id utente preso a caso dal server per vedere se funziona
    fun postWeight(@Body userWeight: Peso) : Call<Peso>



    // I seguenti sono stati aggiunti da Sofia
    @GET("users/{idPaziente}")
    fun getPaziente(@Path("idPaziente") idPaziente: String) : Call<UtenteDataClass>
    //
    @PUT("nut/set-dieta/{idPaziente}")
    fun putDieta(@Path("idPaziente") idPaziente: String, @Body dieta: Dieta) : Call<Dieta>
    // Fine metodi  aggiunti da Sofia
}
