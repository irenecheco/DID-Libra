package it.polito.s279941.libra.professionistapazienti

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utenteobiettivi.ObiettiviViewModel
import kotlinx.android.synthetic.main.professionista_paziente_aggiungi_obiettivo_fragment.*
import java.util.*

class ProfessionistaPazienteAggiungiObiettivoFragment : Fragment(R.layout.professionista_paziente_aggiungi_obiettivo_fragment) {

    private lateinit var viewModel: ObiettiviViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ObiettiviViewModel::class.java)
        Log.d("LIBRA","calling & create the viewModel of class ObiettiviVieModel in ProfessionistaPazienteAggiungiObiettivoFragment")

        val newFragment: Fragment = ProfessionistaPazienteProfiloFragment()
        addGoal_submitButton.setOnClickListener{
            Log.d("LIBRA", "event CLICK on SUBMIT in ProfessionistaPazienteAggiungiObiettivoFragment")

            val dateGoal = Date()
            val inputGoal : String = addGoal_input.text.toString()
            val newGoal = Obiettivo(dateGoal,inputGoal)
            viewModel.addGoal(newGoal)

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.pazienti_fragment_container, newFragment)
            transaction?.addToBackStack("ProfessionistaPazienteAggiungiObiettivoFragment")
            transaction?.commit()
        }
    }
}