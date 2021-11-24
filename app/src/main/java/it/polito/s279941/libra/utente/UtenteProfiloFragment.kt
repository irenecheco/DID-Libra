package it.polito.s279941.libra.utente

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.utente_profilo_fragment.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class UtenteProfiloFragment: Fragment(R.layout.utente_profilo_fragment) {

    private val utenteViewModel: UtenteViewModel by activityViewModels()

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_name.text = utenteViewModel.utenteCorrente.nome
        user_name2.text = utenteViewModel.utenteCorrente.cognome
        nutr_name.text = utenteViewModel.utenteCorrente.cod_nutrizionista
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateString = simpleDateFormat.format(utenteViewModel.utenteCorrente.data_nascita)
        user_dateOfBirth3.text = (dateString).toString()
        id_albo.text = utenteViewModel.utenteCorrente.email
        if(utenteViewModel.utenteCorrente.obiettivi?.size != 0) {
            last_goal.text = utenteViewModel.utenteCorrente.obiettivi?.last()?.obiettivo
        } else {
            Log.d("profilo", "Sto creando il profilo, non ci sono obiettivi")
            last_goal.text = getString(R.string.no_goals_title)
        }
        if(utenteViewModel.utenteCorrente.storico_pesi?.size != 0) {
            text_measure.text = utenteViewModel.utenteCorrente.storico_pesi?.last()?.peso.toString()
            date_last_detection2.text =
                utenteViewModel.utenteCorrente.storico_pesi?.last()?.data.toString()
        } else {
            Log.d("profilo", "Sto creando il profilo, non ci sono pesi")
            text_measure.text = "- KG"
            date_last_detection2.text = "-/-/-"
        }





    }

}