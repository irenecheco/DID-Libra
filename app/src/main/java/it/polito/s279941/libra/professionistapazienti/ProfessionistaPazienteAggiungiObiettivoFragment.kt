package it.polito.s279941.libra.professionistapazienti

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionista.ProfessionistaViewModel
import it.polito.s279941.libra.utils.Status
import kotlinx.android.synthetic.main.professionista_paziente_aggiungi_obiettivo_fragment.*
import java.util.*

class ProfessionistaPazienteAggiungiObiettivoFragment : Fragment(R.layout.professionista_paziente_aggiungi_obiettivo_fragment) {

    private val nutViewModel: ProfessionistaViewModel by activityViewModels()
    private val pazienteViewModel: ProfessionistaPazienteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_goal_Label.text = getString(R.string.nutr_patient_add_goal_Label)
        addGoal_submitButton.visibility = View.VISIBLE
        addGoal_submitButton.text = getString(R.string.goal_submit_button)
        add_goal_input.visibility = View.VISIBLE
        addGoal_input.isFocusable = true
        addGoal_input.isEnabled = true
        add_goal_confirmation.visibility = View.GONE
        progressBarGoal.visibility = View.GONE
        var goalConfirmed = false

        Log.d("LIBRA","calling & create the viewModel of class ObiettiviVieModel in ProfessionistaPazienteAggiungiObiettivoFragment")

        val newFragment: Fragment = ProfessionistaPazienteProfiloFragment()
        // un unico bottone ha due funzioni a seconda di quando viene premuto
        addGoal_submitButton.setOnClickListener{
            Log.d("LIBRA", "event CLICK on SUBMIT in ProfessionistaPazienteAggiungiObiettivoFragment")

            // se è stato scritto qualcosa nell'input il bottone scompare e si chiede conferma di invio
            if(!addGoal_input.text.isNullOrEmpty()) {
                addGoal_input.isFocusable = false
                addGoal_input.isEnabled = false
                add_goal_confirmation.visibility = View.VISIBLE
                addGoal_submitButton.visibility = View.GONE
            }

            // se l'obiettivo è stato confermato e quindi aggiunto al server, il bottone riporta al profilo del paziente
            // infatti anche il testo del bottone è cambiato in "torna alla pagina del paziente"
            if(goalConfirmed){
                activity?.onBackPressed()
            }
        }

        confirm_button.setOnClickListener{
            Log.d("LIBRA", "event CLICK on CONFIRM in ProfessionistaPazienteAggiungiObiettivoFragment")

            val dateGoal = Date()  // la data odierna
            val inputGoal : String = addGoal_input.text.toString()  // l'obiettivo inserito nell'input
            val newGoal = Obiettivo(dateGoal,inputGoal)
            // aggiungo l'obiettivo alla lista degli obiettivi del paziente corrente attraverso la funzione
            // addGoal(idPaziente,userGoal) nel view model del professionista
            nutViewModel.addGoal(pazienteViewModel.pazienteCorrente._id,newGoal)

            // controllo lo stato dell'operazione della riga precedente
            nutViewModel.confirmationAddGoal.observe(viewLifecycleOwner) { goalStatus ->
                when(goalStatus!!){
                    Status.LOADING -> {
                        progressBarGoal.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        progressBarGoal.visibility = View.GONE
                        add_goal_Label.text = getString(R.string.nutr_patient_goal_added_Label)
                        pazienteViewModel.pazienteCorrente.obiettivi?.add(newGoal)
                    }
                    Status.ERROR -> {
                        progressBarGoal.visibility = View.GONE
                        add_goal_Label.text = getString(R.string.nutr_patient_goal_not_added_Label)
                    }
                }
                Log.d("LIBRAgoals", "confirmationStatus in fragment: " + goalStatus.toString())
            }

            addGoal_submitButton.text = getString(R.string.goal_back_button)  // cambio testo del bottone
            add_goal_input.visibility = View.GONE
            addGoal_submitButton.visibility = View.VISIBLE
            add_goal_confirmation.visibility = View.GONE

            goalConfirmed = true
        }

        deny_button.setOnClickListener{
            Log.d("LIBRA", "event CLICK on DENY in ProfessionistaPazienteAggiungiObiettivoFragment")

            add_goal_Label.text = getString(R.string.nutr_patient_add_goal_Label)
            addGoal_submitButton.visibility = View.VISIBLE
            addGoal_submitButton.text = getString(R.string.goal_submit_button)
            add_goal_input.visibility = View.VISIBLE
            addGoal_input.isFocusableInTouchMode = true
            addGoal_input.isEnabled = true
            add_goal_confirmation.visibility = View.GONE
        }
    }
}