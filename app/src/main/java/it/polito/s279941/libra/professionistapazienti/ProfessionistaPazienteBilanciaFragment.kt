package it.polito.s279941.libra.professionistapazienti

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_paziente_bilancia_fragment.*

class ProfessionistaPazienteBilanciaFragment: Fragment(R.layout.professionista_paziente_bilancia_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = libra_lock_spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.nut_libra_lock_options,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
}