package it.polito.s279941.libra.professionistadieta

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_calendariodieta_datainizio.*

import java.util.*

class ProfessionistaCalendarioDietaDataInizio : Fragment(R.layout.professionista_calendariodieta_datainizio)  {
    val professionistaGiorniDietaPazienteViewModel by activityViewModels<ProfessionistaGiorniDietaPazienteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("aaaa","onViewCreated")

        calendarioDataInizio.date = professionistaGiorniDietaPazienteViewModel.getGiornoInizioDieta()
        val gg=professionistaGiorniDietaPazienteViewModel.getGiornoInizioDieta()
        Log.e("date", "Giorno:$gg")

        // TODO: Aggiungere listener evento di selezione giorno calendario
        // val calendarView = view.findViewById(R.id.calendarView)
        // calendarioDieta
        calendarioDataInizio.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
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
            professionistaGiorniDietaPazienteViewModel.setGiornoInizioDieta(cal.timeInMillis)

            Log.e("date", "giornoSelezionato:$cal.timeInMillis")


            // si riposiziona sul fragment della dieta chiudendo il calendario
           // this.activity?.supportFragmentManager?.beginTransaction()?.apply {
           //     replace(R.id.fragment_container, UtenteDietaFragment()).commit()
           // }
            this.activity?.onBackPressed()

            Log.e("date", "$Year/$Month/$curDate")
        })
    }
}