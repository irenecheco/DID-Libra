package it.polito.s279941.libra.professionista

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionistapazienti.PazientiAdapter
import kotlinx.android.synthetic.main.professionista_pazienti_fragment.*

class ProfessionistaPazientiFragment: Fragment(R.layout.professionista_pazienti_fragment) {

    val patientViewModel by activityViewModels<ProfessionistaViewModel>()
    val patientAdapter = PazientiAdapter();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientViewModel.pazientiLiveData.observe(viewLifecycleOwner, Observer { data -> patientAdapter.setPazienti(data) })
        recyclerView_pazienti.layoutManager= LinearLayoutManager(requireContext())
        recyclerView_pazienti.adapter = patientAdapter
    }

}