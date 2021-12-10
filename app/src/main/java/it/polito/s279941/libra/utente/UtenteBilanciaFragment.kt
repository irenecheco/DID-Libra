package it.polito.s279941.libra.utente


import android.content.Context
import android.content.Intent
import android.net.*
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import it.polito.s279941.libra.DataModel.Peso
import it.polito.s279941.libra.DataModel.UtenteAggiornaPesoClass
import it.polito.s279941.libra.DataModel.UtenteAvviaBilanciaClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api2
import it.polito.s279941.libra.utils.LOG_TAG_ESP
import it.polito.s279941.libra.utils.Status
import kotlinx.android.synthetic.main.utente_bilancia_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.text_measure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO : l'app va in crash quando si ruota il telefono

class UtenteBilanciaFragment: Fragment(R.layout.utente_bilancia_fragment) {

    private val viewModel: UtenteViewModel by activityViewModels()
    var weight : Double = 0.0
    var flag_lettura = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The singleton Api object is created lazily when the first time it is used.
        // After that it will be reused without creation
        val apiServe by lazy {
            Api2.create()
        }

        try {
            //text_measure.text=viewModel.utenteCorrente.storico_pesi?.last()?.peso.toString() + " kg"
            // formatto a una cifra decimale
            text_measure.text = String.format("%.1f kg", viewModel.utenteCorrente.storico_pesi?.last()?.peso)
        }catch (e: NoSuchElementException){
            val bottomNavView: View? = activity?.findViewById(R.id.bottom_bar)
            Snackbar.make(bottomNavView!!, R.string.snackbar_no_measure, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().resources.getColor(R.color.colorSnackbar)).setAnchorView(bottomNavView).show()
            text_measure.text = "- kg"
        }

        val manager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()

        flag_lettura = 0

        registra_peso.isEnabled = false
        disconnetti_bilancia.isEnabled = false

        //Con il tasto avvia bilancia inizio il procedimento di collegamento alla bilancia
        avvia_bilancia.setOnClickListener {
            Log.d(LOG_TAG_ESP, "Click on AVVIA bilancia")
            text_measure.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
            calibrationLink_tv.visibility = View.INVISIBLE

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
                            val ultimarilevazioneView: View? = activity?.findViewById(R.id.textview_ultima_rilevazione)
                            Snackbar.make(ultimarilevazioneView!!, R.string.snackbar_follow_libra, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().resources.getColor(R.color.colorSnackbar)).setAnchorView(ultimarilevazioneView).show()

                            // inoltro la richiesta al S.O. e implemento la callback per gestire la risposta
                            // la get si aspetta in risposta un oggetto di tipo UtenteAvviaBilanciaClass
                            initScale.enqueue(object : Callback<UtenteAvviaBilanciaClass> {
                                override fun onResponse(
                                    call: Call<UtenteAvviaBilanciaClass>?,
                                    response: Response<UtenteAvviaBilanciaClass>?
                                ) {
                                    //if(response?.body() != null)
                                    if (response?.isSuccessful == true) {

                                        // posso recuperare e registrare il peso acquisito
                                        avvia_bilancia.visibility = View.GONE
                                        registra_peso.visibility = View.VISIBLE
                                        registra_peso_label.visibility = View.VISIBLE
                                        disconnetti_bilancia.visibility = View.VISIBLE
                                        disconnetti_bilancia_label.visibility = View.VISIBLE
                                        Log.d(
                                            LOG_TAG_ESP,
                                            "initScale: response.isSuccessful=true -> Bilancia attiva"
                                        )
                                        registra_peso?.isEnabled = true
                                        disconnetti_bilancia.isEnabled = true
                                        avvia_bilancia?.isEnabled = false
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

        //Con il tasto registra_peso (LEGGI PESO) faccio una get per acquisire il peso dalla bilancia
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

                                // peso recuperato, devo elaborare il json
                                Log.d(LOG_TAG_ESP,"response code: " + response.code())
                                Log.d(LOG_TAG_ESP,"getWeight: response.isSuccessful=true -> PESO acquisito")

                                // response.body() è oggetto della classe UtenteAggiornaPesoClass
                                weight = response.body()!!.get_weight // '!!' forza cast da tipo "?Double" a "Double"
                                Log.d(LOG_TAG_ESP, "weight: " + weight + "| type is double: " + (weight is Double).toString())

                                // nascondo la progress_bar e visualizzo il peso
                                progress_bar.visibility = View.GONE
                                //text_measure.text = weight.toString() + " kg"
                                text_measure.text = String.format("%.1f kg", weight)
                                text_measure.visibility = View.VISIBLE
                                flag_lettura = 1

                                //salvo peso in locale
                                var today = Calendar.getInstance().time

                                var ultimo = viewModel.utenteCorrente.storico_pesi?.lastIndex!!.toInt()
                                Log.d("Ultimo", "ultimo indice è " + ultimo)

                                if (ultimo >= 0) {
                                    Log.d("Ultimo", "dentro IF")
                                    var new = Calendar.getInstance()
                                    new.setTime(today)
                                    var old = Calendar.getInstance()
                                    old.setTime(viewModel.utenteCorrente.storico_pesi?.last()?.data)

                                    Log.d("Ultimo", "new.setTime= ${new.get(Calendar.YEAR)}  ${new.get(
                                        java.util.Calendar.MONTH)}  ${new.get(java.util.Calendar.DAY_OF_MONTH)}")

                                    Log.d("Ultimo", "old.setTime= ${old.get(Calendar.YEAR)}  ${old.get(
                                        java.util.Calendar.MONTH)}  ${old.get(java.util.Calendar.DAY_OF_MONTH)}")

                                    if (new.get(Calendar.YEAR) == old.get(Calendar.YEAR)) {
                                        if (new.get(Calendar.MONTH) == old.get(Calendar.MONTH)) {
                                            if (new.get(Calendar.DAY_OF_MONTH) == old.get(Calendar.DAY_OF_MONTH)) {
                                                viewModel.utenteCorrente.storico_pesi?.set(ultimo, Peso(today, weight))

                                                Log.d("Calendar", "stessa data, sovrascrivo")
                                                Log.d("nuovo vettore", "nuovo vettore è " + viewModel.utenteCorrente.storico_pesi)
                                            } else {
                                                viewModel.utenteCorrente.storico_pesi?.add(Peso(today, weight))
                                                Log.d("Calendar", "diversa data (giorno), aggiungo")
                                                Log.d("nuovo vettore", "nuovo vettore è " + viewModel.utenteCorrente.storico_pesi)
                                            }
                                        } else {
                                            viewModel.utenteCorrente.storico_pesi?.add(Peso(today, weight))
                                            Log.d("Calendar", "diversa data (mese), aggiungo")
                                            Log.d("nuovo vettore", "nuovo vettore è " + viewModel.utenteCorrente.storico_pesi)
                                        }
                                    } else {
                                        viewModel.utenteCorrente.storico_pesi?.add(Peso(today, weight))
                                        Log.d("Calendar", "diversa data (anno), aggiungo")
                                        Log.d("nuovo vettore", "nuovo vettore è " + viewModel.utenteCorrente.storico_pesi)
                                    }
                                } else {
                                    Log.d("Ultimo", "dentro ELSE ")
                                    viewModel.utenteCorrente.storico_pesi?.add(Peso(today, weight))
                                    Log.d("Calendar", "array vuoto, aggiungo")
                                    Log.d("nuovo vettore", "nuovo vettore è " + viewModel.utenteCorrente.storico_pesi)
                                }
                                Log.d("Ultimo", "FINE FUNZIONE LEGGI PESO ==========================")
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

            } catch (e: SecurityException) {
                Log.e(LOG_TAG_ESP, "try/catch on getWeight, exception catched:" + e.message!!)
                Log.d(LOG_TAG_ESP, "try/catch on getWeight, exception catched:" + e.message!!)
            }
        }

        //disconnette bilancia e fa post al server
        disconnetti_bilancia.setOnClickListener(){
            if(flag_lettura ==1) {

                //unbind process from Libra, può connettersi a internet con i dati del telefono

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.bindProcessToNetwork(null)
                }

                //recupero data odierna
                var today = Calendar.getInstance().time

                if (weight > 0) {
                    //lancio la funzione post weight contenuta in UtenteBilanciaViewModel per fare POST al server
                    viewModel.postWeight(today, weight)
                    Log.d("BILANCIA", "Lanciata post")
                    viewModel.confirmation.observe(viewLifecycleOwner) { weightStatus ->
                        when(weightStatus!!){
                            Status.SUCCESS -> {
                                val bottomNavView: View? = activity?.findViewById(R.id.bottom_bar)
                                Snackbar.make(bottomNavView!!, R.string.snackbar_saved_weight, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().resources.getColor(R.color.colorSnackbar)).setAnchorView(bottomNavView).show()
                                registra_peso.visibility = View.GONE
                                registra_peso_label.visibility = View.GONE
                                disconnetti_bilancia.visibility = View.GONE
                                disconnetti_bilancia_label.visibility = View.GONE
                                avvia_bilancia.visibility = View.VISIBLE

                                avvia_bilancia?.isEnabled = true
                                registra_peso?.isEnabled = false
                                disconnetti_bilancia?.isEnabled = false
                            }
                            Status.ERROR -> {
                                val bottomNavView: View? = activity?.findViewById(R.id.bottom_bar)
                                Snackbar.make(bottomNavView!!, R.string.snackbar_error_weight, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().resources.getColor(R.color.colorSnackbar)).setAnchorView(bottomNavView).show()
                            }
                        }
                        Log.d("LIBRAgoals", "confirmationStatus in fragment: " + weightStatus.toString())
                    }
                }
            } else {
                val bottomNavView: View? = activity?.findViewById(R.id.bottom_bar)
                Snackbar.make(bottomNavView!!, R.string.snackbar_read_first, Snackbar.LENGTH_SHORT).setBackgroundTint(requireContext().resources.getColor(R.color.colorSnackbar)).setAnchorView(bottomNavView).show()
            }
        }

        calibrationLink_tv.setOnClickListener {
            // creo un intent per aprire il browser e scaricare il pdf ccon le istruzioni di calibrazione
            val url = "https://drive.google.com/file/d/1JFBIP35yRLzonqK1woXRDbxQr2q1geYA/view?usp=sharing"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}