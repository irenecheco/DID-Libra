package it.polito.s279941.libra.utente


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import it.polito.s279941.libra.DataModel.UtenteAggiornaPesoClass
import it.polito.s279941.libra.DataModel.UtenteAvviaBilanciaClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api2
import it.polito.s279941.libra.landing.LandingPageViewModel
import it.polito.s279941.libra.utils.LOG_TAG_ESP
import kotlinx.android.synthetic.main.utente_bilancia_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.text_measure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO : l'app va in crash quando si ruota il telefono
// TODO: al  termine della lettura l'app deve collegarsi al DB e salvare il peso, quindi deve
//  scollegarsi dal wifi bilancia e ricollegarsi a quello precedente !

class UtenteBilanciaFragment: Fragment(R.layout.utente_bilancia_fragment) {

    private val viewModel: UtenteBilanciaViewModel by activityViewModels()

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
        //controllo permessi per recuperare ssid del wifi
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //se localizzazione non abilitata, chiedo all'utente di abilitarla e poi recupero ssid
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            val wifi_manager : WifiManager = requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            var wifiInfo : WifiInfo = wifi_manager.connectionInfo
            var wifi_ssid : String = wifiInfo.ssid
            Log.d("LIBRA", "wifi ssid è " + wifi_ssid)
        }else{
            //se localizzazione abilitata recupero direttamente ssid
            val wifi_manager : WifiManager = requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            var wifiInfo : WifiInfo = wifi_manager.connectionInfo
            var wifi_ssid : String = wifiInfo.ssid
            Log.d("LIBRA", "wifi ssid è " + wifi_ssid)
        }
        var weight : Double = 0.0

        registra_peso.isEnabled = false

        //Con il tasto avvia bilancia inizio il procedimento di collegamento alla bilancia
        avvia_bilancia.setOnClickListener {
            //text_measure.text = "Pulsante funziona"
            Log.d(LOG_TAG_ESP, "Click on AVVIA bilancia")
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

            Log.d(LOG_TAG_ESP, "NetworkRequest.Builder built")
            //text_measure.text = "Builder built"
            try {
                manager.requestNetwork(builder.build(), object : ConnectivityManager.NetworkCallback() {
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onAvailable(network: Network) {
                            manager.bindProcessToNetwork(network)
                            Log.d(LOG_TAG_ESP, "network connected")
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
                                            Log.d(LOG_TAG_ESP,
                                                "initScale: response.isSuccessful=true -> Bilancia attiva")
                                            // TODO: il bottone si deve attivare e/o visualizzare solo se previsto dal nutrizionista!
                                            registra_peso?.isEnabled = true
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<UtenteAvviaBilanciaClass>?, t: Throwable?,
                                    ) {
                                        Log.d(LOG_TAG_ESP, "initScale: response FAIL")
                                    }
                                })
                            }
                        }
                    })
            } catch (e: SecurityException) {
                Log.e(LOG_TAG_ESP, e.message!!)
                progress_bar.visibility = View.GONE
                text_measure.text = "Error"
                text_measure.visibility = View.VISIBLE
            }
        }

        //Con il tasto registra_peso faccio una get per acquisire il peso dalla bilancia
        registra_peso.setOnClickListener() {
            Log.d(LOG_TAG_ESP, "Click on REGISTRA PESO")
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
                                //recupero data odierna
                                var today = Calendar.getInstance().time
                                // peso recuperato, devo elaborare il json
                                Log.d(LOG_TAG_ESP,"response code: " + response.code())
                                Log.d(LOG_TAG_ESP,"getWeight: response.isSuccessful=true -> PESO acquisito")
                                // response.body() è oggetto della classe UtenteAggiornaPesoClass
                                weight = response.body()!!.get_weight // '!!' forza cast da tipo "?Double" a "Double"
                                Log.d(LOG_TAG_ESP, "weight: " + weight + "| type is double: " + (weight is Double).toString())
                                // nascondo la progress_bar e visualizzo il peso
                                progress_bar.visibility = View.GONE
                                text_measure.text = weight.toString()
                                text_measure.visibility = View.VISIBLE
                                //lancio la funzione post weight contenuta in UtenteBilanciaViewModel per fare POST al server
                                viewModel.postWeight(today, weight)
                            }
                        }
                        override fun onFailure(
                            call: Call<UtenteAggiornaPesoClass>?,
                            t: Throwable?,
                        ) {
                            Log.d(LOG_TAG_ESP, "getWeight: unable fetch weight")
                        }
                    })
                }


                // disconnessione dalla rete wifi bilancia
                //builder.removeTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            } catch (e: SecurityException) {
                Log.e(LOG_TAG_ESP, "try/catch on getWeight, exception catched:" + e.message!!)
                Log.d(LOG_TAG_ESP, "try/catch on getWeight, exception catched:" + e.message!!)
            }
        }

    }
}



/*
 /*override fun onResponse(call: Call<String>?, response: Response<String>) {
                                    Log.d(LOG_TAG_ESP,"call  onResponse  (from initScale request)")
                                    if (response.isSuccessful) {
                                        //Bilancia attiva
                                        //Abilito il pulsante Aggiorna Peso
                                        Log.d(LOG_TAG_ESP,"initScale: response.isSuccessful=true -> Bilancia attiva")
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
                                                        Log.d(LOG_TAG_ESP,"getWeight: response.isSuccessful=true -> Acquisizione dati ok")
                                                        Log.d(LOG_TAG_ESP,"weight: " + response.body().toString())
                                                        *//*try {
                                                            //Trasformo la risposta da json a Double e lo salvo in weight
                                                            val jsonString = response.body().toString()
                                                            val weight = Gson().fromJson(jsonString, UtenteAggiornaPesoClass::class.java)
                                                            //Stampo peso
                                                            text_measure.text =
                                                                weight.get_weight.toString()
                                                        } catch (e: JSONException) {
                                                            Log.d(LOG_TAG_ESP,
                                                                "ERROR getting JSON object to show weight")
                                                        }*//*
                                                    }
                                                }
                                                override fun onFailure(call: Call<Number>, t: Throwable) {
                                                    Log.d(LOG_TAG_ESP, "ERROR contacting the libra to get the weight")
                                                }

                                            })
                                        }

                                    }
                                }
                                override fun onFailure(call: Call<String>?, t: Throwable?){
                                    Log.d(LOG_TAG_ESP, "ERROR contacting the libra to turn it on")
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