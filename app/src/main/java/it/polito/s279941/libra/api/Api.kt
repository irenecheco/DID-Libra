package it.polito.s279941.libra.api

import io.reactivex.Observable
import it.polito.s279941.libra.DataModel.UtenteAggiornaPesoClass
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.utenteobiettivi.ObiettiviItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import it.polito.s279941.libra.DataModel.UtenteAvviaBilanciaClass
import it.polito.s279941.libra.utils.BACKEND_URL
import it.polito.s279941.libra.utils.ESP8266_URL

interface Api {

    companion object {
        // Set NodeJS server IP:port (solitamente il PC su cui gira NodeJS e AndroidStudio),
        // ovviamente l'IP deve essere raggiungibile dall'app
        // The NodeJS server IP (solitamente il PC su cui gira NodeJS e AndroidStudio)
        val BASE_URL = BACKEND_URL

        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(Api::class.java)
        }
    }

    @GET("users")
    //fun getUsers() : Call<List<UtenteDataClass>>
    fun getUsers() : Call<String>

    @GET("users")
    fun getUsers2() : Observable<List<UtenteDataClass>>

    @GET("users/goals/6071aea342e7530e8c1947ed") // id utente preso a caso dal server per vedere se funziona
    // poi ci sarà da creare una variabile che indica l'utente che sta usando l'app da incatenare al resto dell'indirizzo
    fun getGoals() : Call<List<ObiettiviItem>>

}

interface Api2 {

    companion object {
        // *** ATTENZIONE il cell deve avere WiFi attiva *** //
        // URL della ESP8266
        val BASE_URL = ESP8266_URL

        fun create(): Api2 {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(Api2::class.java)
        }
    }

    @GET("init_scale")
    //fun initScale(): Call<String>
     fun initScaleREST(): Call<UtenteAvviaBilanciaClass>

    @GET("get_weight")
    fun getWeightREST(): Call<UtenteAggiornaPesoClass>
}



/*
interface Api {
    */
/**
     * The return type is important here
     * The class structure that you've defined in Call<T>
     * should exactly match with your json response
     *//*

    @get:GET("users")
    val users: Call<List<UtenteDataClass?>?>?

    companion object {
        const val BASE_URL = "https://192.168.225.240/api/"
    }
}*/
