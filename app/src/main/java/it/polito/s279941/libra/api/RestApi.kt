package it.polito.s279941.libra.api

import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

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

    @GET("users/goals/6071aea342e7530e8c1947ed") // id utente preso a caso dal server per vedere se funziona
    // poi ci sarà da creare una variabile che indica l'utente che sta usando l'app da incatenare al resto dell'indirizzo
    fun getGoals() : Call<List<ObiettiviItem>>*/

}
