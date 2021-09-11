package it.polito.s279941.libra.utente


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import it.polito.s279941.libra.DataModel.UtenteAggiornaPesoClass
import it.polito.s279941.libra.DataModel.UtenteAvviaBilanciaClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api2
import kotlinx.android.synthetic.main.utente_bilancia_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.text_measure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO : l'app va in crash quando si ruota il telefono
// TODO: al  termine della lettura l'app deve collegarsi al DB e salvare il peso, quindi deve
//  scollegarsi dal wifi bilancia e ricollegarsi a quello precedente !

class UtenteBilanciaFragment: Fragment(R.layout.utente_bilancia_fragment) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The singleton Api object is created lazily when the first time it is used.
        // After that it will be reused without creation
        val apiServe by lazy {
            Api2.create()
        }

        val manager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        var weight : Double = 0.0

        registra_peso.isEnabled = false

        //Con il tasto avvia bilancia inizio il procedimento di collegamento alla bilancia
        avvia_bilancia.setOnClickListener {
            //text_measure.text = "Pulsante funziona"
            Log.d("ESP8266", "Click on AVVIA bilancia")
            text_measure.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE

                builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                builder.removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    builder.setNetworkSpecifier(
                        WifiNetworkSpecifier.Builder().apply {
                            //Qui inserite il nome del vostro WIFI e la password
                            setSsid("LibraNET")
                            setWpa2Passphrase("libranet")
                        }.build()
                    )
                }

            Log.d("ESP8266", "NetworkRequest.Builder built")
            //text_measure.text = "Builder built"
            try {
                manager.requestNetwork(builder.build(), object : ConnectivityManager.NetworkCallback() {
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onAvailable(network: Network) {
                            manager.bindProcessToNetwork(network)
                            Log.d("ESP8266", "network connected")
                            // avvio la fase di attivazione bilancia in uno scope dedicato all'I/O
                            lifecycleScope.launch(Dispatchers.IO) {
                                //Inizializzo la richiesta GET per attivare bilancia
                                val initScale = apiServe.initScaleREST()

                                // inoltro la richiesta al S.O. e implemento la callback per gestire la risposta
                                // la get si aspetta in risposta un oggetto di tipo UtenteAvviaBilanciaClass
                                initScale.enqueue(object : Callback<UtenteAvviaBilanciaClass> {
                                    override fun onResponse(call: Call<UtenteAvviaBilanciaClass>?, response: Response<UtenteAvviaBilanciaClass>?) {
                                        //if(response?.body() != null)
                                        if (response?.isSuccessful == true) {
                                            // posso recuperare e registrare il peso acquisito
                                            Log.d("ESP8266",
                                                "initScale: response.isSuccessful=true -> Bilancia attiva")
                                            // TODO: il bottone si deve attivare e/o visualizzare solo se previsto dal nutrizionista!
                                            registra_peso?.isEnabled = true
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<UtenteAvviaBilanciaClass>?, t: Throwable?,
                                    ) {
                                        Log.d("ESP8266", "initScale: response FAIL")
                                    }
                                })
                            }
                        }
                    })
            } catch (e: SecurityException) {
                Log.e("Ciao", e.message!!)
                progress_bar.visibility = View.GONE
                text_measure.text = "Error"
                text_measure.visibility = View.VISIBLE
            }
        }

        //Con il tasto registra_peso faccio una get per acquisire il peso dalla bilancia
        registra_peso.setOnClickListener() {
            Log.d("ESP8266", "Click on REGISTRA PESO")
            try {
                // Inizializzo la richiesta GET per registrare il peso
                val getWeight = apiServe.getWeightREST()
                // inoltro la richiesta al S.O. e implemento la callback per gestire la risposta
                // la get si aspetta in risposta un oggetto di tipo UtenteAvviaBilanciaClass
                // avvio la fase di acquisizione peso in uno scope dedicato all'I/O
                lifecycleScope.launch(Dispatchers.IO) {
                    getWeight.enqueue(object : Callback<UtenteAggiornaPesoClass> {
                        override fun onResponse(call: Call<UtenteAggiornaPesoClass>?, response: Response<UtenteAggiornaPesoClass>?) {
                            //if(response?.body() != null)
                            if (response?.isSuccessful == true) {
                                // peso recuperato, devo elaborare il json
                                Log.d("ESP8266",
                                    "getWeight: response.isSuccessful=true -> PESO acquisito")
                                // response.body() è oggetto della classe UtenteAggiornaPesoClass
                                weight = response.body()!!.get_weight // '!!' forza cast da tipo "?Double" a "Double"
                                Log.d("ESP8266", "weight: " + weight + "| type is double: " + (weight is Double).toString())
                                // nascondo la progress_bar e visualizzo il peso
                                progress_bar.visibility = View.GONE
                                text_measure.text = weight.toString()
                                text_measure.visibility = View.VISIBLE
                            }
                        }
                        override fun onFailure(
                            call: Call<UtenteAggiornaPesoClass>?,
                            t: Throwable?,
                        ) {
                            Log.d("ESP8266", "getWeight: unable fetch weight")
                        }
                    })
                }


                // disconnessione dalla rete wifi bilancia
                //builder.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            } catch (e: SecurityException) {
                Log.e("ESP8266", "try/catch on getWeight, exception catched:" + e.message!!)
                Log.d("ESP8266", "try/catch on getWeight, exception catched:" + e.message!!)
            }
        }

    }
}



/*
 /*override fun onResponse(call: Call<String>?, response: Response<String>) {
                                    Log.d("ESP8266","call  onResponse  (from initScale request)")
                                    if (response.isSuccessful) {
                                        //Bilancia attiva
                                        //Abilito il pulsante Aggiorna Peso
                                        Log.d("ESP8266","initScale: response.isSuccessful=true -> Bilancia attiva")
                                        progress_bar.visibility = View.GONE
                                        text_measure.visibility = View.VISIBLE
                                        this@UtenteBilanciaFragment.avvia_bilancia.visibility = View.GONE
                                        this@UtenteBilanciaFragment.aggiorna_peso.visibility = View.VISIBLE
                                        //Premendo il tasto di registra peso avvio procedura per stampare peso
                                        aggiorna_peso.setOnClickListener{
                                            //Faccio la GET per prendere il peso
                                            val getWeight = apiServe.getWeightREST()
                                            getWeight.enqueue(object: Callback<Number> {
                                                override fun onResponse(call: Call<Number>, response: Response<Number>) {
                                                    if (response.isSuccessful) {
                                                        Log.d("ESP8266","getWeight: response.isSuccessful=true -> Acquisizione dati ok")
                                                        Log.d("ESP8266","weight: " + response.body().toString())
                                                        *//*try {
                                                            //Trasformo la risposta da json a Double e lo salvo in weight
                                                            val jsonString = response.body().toString()
                                                            val weight = Gson().fromJson(jsonString, UtenteAggiornaPesoClass::class.java)
                                                            //Stampo peso
                                                            text_measure.text =
                                                                weight.get_weight.toString()
                                                        } catch (e: JSONException) {
                                                            Log.d("LIBRA",
                                                                "ERROR getting JSON object to show weight")
                                                        }*//*
                                                    }
                                                }
                                                override fun onFailure(call: Call<Number>, t: Throwable) {
                                                    Log.d("LIBRA", "ERROR contacting the libra to get the weight")
                                                }

                                            })
                                        }

                                    }
                                }
                                override fun onFailure(call: Call<String>?, t: Throwable?){
                                    Log.d("LIBRA", "ERROR contacting the libra to turn it on")
                                }
                            })*/

                                /*val str= URL("http://192.168.4.1/").readText(Charset.forName("UTF-8"))
                            withContext(Dispatchers.Main) {
                                progress_bar.visibility = View.GONE
                                text_measure.visibility = View.VISIBLE
                                //qui mi faccio mostrare il peso in una textView
                                text_measure.text = str;
                            }*/
*/