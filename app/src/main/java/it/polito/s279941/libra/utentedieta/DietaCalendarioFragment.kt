package it.polito.s279941.libra.utentedieta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utente.UtenteViewModel
import kotlinx.android.synthetic.main.utente_dieta_calendario_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import java.text.DateFormat
import java.util.*
import android.widget.CalendarView

import android.widget.CalendarView.OnDateChangeListener
import it.polito.s279941.libra.utente.UtenteDietaFragment
import java.time.LocalDate


class DietaCalendarioFragment: Fragment(R.layout.utente_dieta_calendario_fragment) {
    // utenteViewModel by activityViewModels<UtenteViewModel>()
    val utenteViewModel by activityViewModels<UtenteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("aaaa","onViewCreated")

        calendarioDieta.date = utenteViewModel.getGiorno()
        val gg=utenteViewModel.getGiorno()
        Log.e("date", "Giorno:$gg")

        // TODO: Aggiungere listener evento di selezione giorno calendario
       // val calendarView = view.findViewById(R.id.calendarView)
       // calendarioDieta
        calendarioDieta.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            val curDate = dayOfMonth.toString()
            val Year = year.toString()
            val Month = month.toString()
            // TODO: Trasformare yesr, month e dayofmonth in formato data long, come lo accetta il setgiorno del view model
            // nel view model aggiungerre il codice per gestire un giorno come live data e poi impostare il giorno
            // chiudere il frammento del calendario per tornare alla vista del pasto, dovrebbe comparire il nuovo giorno selezionato
            //
            // se funziona poi evolvere aggiungendo prima della chiusura di questo frammento:
            // caricare dal DB la dieta del giorno ritornato e impostare nel view model anche la nuova diete oltre al giorno


            // Imposta il nuovo giorno selezionato nel view model
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            utenteViewModel.setGiorno(cal.timeInMillis)

            Log.e("date", "giornoSelezionato:$cal.timeInMillis")



            // si riposiziona sul fragment della dieta chiudendo il calendario
            this.activity?.supportFragmentManager?.beginTransaction()?.apply{
                replace(R.id.fragment_container, UtenteDietaFragment()).commit()
            }

            Log.e("date", "$Year/$Month/$curDate")
        })
    }

}