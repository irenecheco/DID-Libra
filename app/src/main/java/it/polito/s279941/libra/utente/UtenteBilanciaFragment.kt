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
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api2
import kotlinx.android.synthetic.main.utente_bilancia_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.text_measure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UtenteBilanciaFragment: Fragment(R.layout.utente_bilancia_fragment) {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiServe by lazy {
            Api2.create()
        }

        avvia_bilancia.setOnClickListener {
            //text_measure.text = "Pulsante funziona"

            text_measure.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
            val manager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val builder = NetworkRequest.Builder()
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
            Log.d("esp", "Builder built")
            //text_measure.text = "Builder built"
            try {
                manager.requestNetwork(builder.build(), object : ConnectivityManager.NetworkCallback() {
                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun onAvailable(network: Network) {
                        manager.bindProcessToNetwork(network)
                        Log.d("esp","network connected")
                        lifecycleScope.launch(Dispatchers.IO) {
                            var init_scale =  apiServe.initScale()
                            init_scale.enqueue(object: Callback<Number> {
                                override fun onResponse(call: Call<Number>?, response: Response<Number>?){
                                    progress_bar.visibility = View.GONE
                                    text_measure.visibility = View.VISIBLE
                                    if (response != null) {
                                        avvia_bilancia.visibility = View.GONE
                                        aggiorna_peso.visibility = View.VISIBLE

                                    }
                                }
                                override fun onFailure(call: Call<Number>?, t: Throwable?){

                                }
                            })
                            /*val str= URL("http://192.168.4.1/").readText(Charset.forName("UTF-8"))
                            withContext(Dispatchers.Main) {
                                progress_bar.visibility = View.GONE
                                text_measure.visibility = View.VISIBLE
                                //qui mi faccio mostrare il peso in una textView
                                text_measure.text = str;
                            }*/
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

        registra_peso.isEnabled = false
    }

}


private fun <T> Call<T>.enqueue(callback: Callback<Number>) {

}
