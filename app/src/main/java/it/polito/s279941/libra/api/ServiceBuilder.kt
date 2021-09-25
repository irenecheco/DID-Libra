package it.polito.s279941.libra.api

import android.util.Log
import it.polito.s279941.libra.utils.BACKEND_URL
import it.polito.s279941.libra.utils.LOG_TAG
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a

We create a builder of the retrofit object which can be reused
for all method calls declared in the RestApi interface.
We can set many properties like, converter factory
for json parsing, base url, http client and many more
configurations as required.
Here is the simplest required form for our tasks.

Methods to build retrofit service :
- BaseUrl: set the API endpoint.
- ConverterFactory: responsible to convert Gson response to Serialized Java object.
- CallAdapterFactory: before talk about Call Adapter, you must know that retrofit
                    execute HTTP request in background thread.
                    Because It pushes the meaningless response bytes through converters
                    and wraps it into a usable response with meaningful Java objects.
                    Call Adapter is in charge to receives and prepares the result,
                    to the Android UI thread.
Client: add personalized HttpClient.
 */

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BACKEND_URL) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        Log.d(LOG_TAG, "start fun  buildService()  in object ServiceBuilder") //--->DBG
        return retrofit.create(service)
    }
}