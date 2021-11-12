package it.polito.s279941.libra.api

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

    // http://BACKEND_URL:3000/api/auth/register (POST con json dei dati utente)
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun signin(@Body utenteSigninData: UtenteSigninData) : Call<UtenteDataClass>

    /*@GET("users")
    fun getUsers2() : Observable<List<UtenteDataClass>>*/


    // OBIETTIVI
    //@GET("users/goals/{idPaziente}")
    //fun getGoals(@Path("idPaziente") idPaziente: String) : Call<List<Obiettivo>>

    @POST("nut/add-goal/{idPaziente}")
    fun addGoal(@Path("idPaziente") idPaziente: String, @Body userData: Obiettivo) : Call<Obiettivo>


    @PUT("users/add-measurement/{idPaziente}")
    fun postWeight(@Path("idPaziente") idPaziente: String, @Body userWeight: Peso) : Call<Peso>



    // I seguenti sono stati aggiunti da Sofia
    @GET("users/{idPaziente}")
    fun getPaziente(@Path("idPaziente") idPaziente: String) : Call<UtenteDataClass>
    //
    @POST("nut/set-dieta/{idPaziente}")
    fun putDieta(@Path("idPaziente") idPaziente: String, @Body dieta: Dieta) : Call<Dieta>
    // Fine metodi  aggiunti da Sofia

    // POST http://localhost:3000/api/users/set-comment/6071aea342e7530e8c1947ed/2020-04-23
    @POST("users/set-comment/{idPaziente}/{data}")
    fun postCommento(@Path("idPaziente") idPaziente: String,@Path("data") data: String, @Body note: CommentoDietaPerUpdateDB) : Call<Unit>

    @POST("users/set-check-pasto/{idPaziente}/{data}/{nomePasto}") // /:idPaziente/:data/:nomePasto
    fun postCheckPasto(@Path("idPaziente") idPaziente: String,@Path("data") data: String, @Path("nomePasto") nomePasto: String, @Body checkPasto: CheckPastoPerUpdateDB) : Call<Unit>

}
