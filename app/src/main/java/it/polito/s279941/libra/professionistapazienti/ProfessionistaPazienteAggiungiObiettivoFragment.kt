package it.polito.s279941.libra.professionistapazienti

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionista.ProfessionistaViewModel
import kotlinx.android.synthetic.main.professionista_paziente_aggiungi_obiettivo_fragment.*
import java.util.*

class ProfessionistaPazienteAggiungiObiettivoFragment : Fragment(R.layout.professionista_paziente_aggiungi_obiettivo_fragment) {

    //private lateinit var viewModel: ObiettiviViewModel
    private lateinit var viewModel: ProfessionistaViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_goal_Label.text = getString(R.string.nutr_patient_add_goal_Label)
        addGoal_submitButton.visibility = View.VISIBLE
        addGoal_submitButton.text = getString(R.string.goal_submit_button)
        add_goal_input.visibility = View.VISIBLE
        add_goal_confirmation.visibility = View.GONE
        addGoal_input.isFocusable = true  //TODO da emulatore funge, ma da telefono no --> da rivedere
        var goalConfirmed = false

        viewModel = ViewModelProvider(this).get(ProfessionistaViewModel::class.java)
        Log.d("LIBRA","calling & create the viewModel of class ObiettiviVieModel in ProfessionistaPazienteAggiungiObiettivoFragment")

        val newFragment: Fragment = ProfessionistaPazienteProfiloFragment()
        addGoal_submitButton.setOnClickListener{
            Log.d("LIBRA", "event CLICK on SUBMIT in ProfessionistaPazienteAggiungiObiettivoFragment")

            if(!addGoal_input.text.isNullOrEmpty()) {
                add_goal_confirmation.visibility = View.VISIBLE
                addGoal_submitButton.visibility = View.GONE
                addGoal_input.isFocusable = false
            }

            if(goalConfirmed){
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.pazienti_fragment_container, newFragment)
                transaction?.addToBackStack("ProfessionistaPazienteAggiungiObiettivoFragment")
                transaction?.commit()
            }
        }

        confirm_button.setOnClickListener{
            Log.d("LIBRA", "event CLICK on CONFIRM in ProfessionistaPazienteAggiungiObiettivoFragment")

            // TODO bisogna controllare che effettivamente l'obiettivo sia stato postato correttamente e in caso contrario dare relativo feeedback

            val dateGoal = Date()
            val inputGoal : String = addGoal_input.text.toString()
            val newGoal = Obiettivo(dateGoal,inputGoal)
            viewModel.addGoal(newGoal)

            add_goal_Label.text = getString(R.string.nutr_patient_goal_added_Label)
            addGoal_submitButton.text = getString(R.string.goal_back_button)
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
            add_goal_confirmation.visibility = View.GONE
            addGoal_input.isFocusable = true
        }
    }
}