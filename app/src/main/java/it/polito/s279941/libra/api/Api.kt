package it.polito.s279941.libra.api

import io.reactivex.Observable
import it.polito.s279941.libra.DataModel.UtenteDataClass
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST


interface Api {

    companion object {
        var BASE_URL = "https://192.168.225.240/api/"

        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
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

}

interface Api2 {

    companion object {
        var BASE_URL2 = " http://softAPIP/libra/"

        fun create(): Api2 {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl(BASE_URL2)
                    .build()

            return retrofit.create(Api2::class.java)
        }
    }

    @GET("init_scale")
    fun initScale() : Call<String>

    @GET("get_weight")
    fun getWeight() : Call<Number>

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
