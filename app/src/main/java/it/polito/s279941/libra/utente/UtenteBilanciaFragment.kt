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
import kotlinx.android.synthetic.main.utente_bilancia_fragment.*
import kotlinx.android.synthetic.main.utente_chat_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.text_measure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.nio.charset.Charset


class UtenteBilanciaFragment: Fragment(R.layout.utente_bilancia_fragment) {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        avvia_bilancia.setOnClickListener {
            text_measure.text = "Pulsante funziona"
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
            text_measure.text = "Builder built"
            try {
                manager.requestNetwork(builder.build(), object : ConnectivityManager.NetworkCallback() {
                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun onAvailable(network: Network) {
                        manager.bindProcessToNetwork(network)
                        Log.d("esp","network connected")
                        lifecycleScope.launch(Dispatchers.IO) {
                            val str= URL("http://192.168.4.1/").readText(Charset.forName("UTF-8"))
                            withContext(Dispatchers.Main) {
                                //qui mi faccio mostrare il peso in una textView
                                text_measure.text = str;
                            }
                        }

                    }
                })
            } catch (e: SecurityException) {
                Log.e("Ciao", e.message!!)

            }
        }
    }

}