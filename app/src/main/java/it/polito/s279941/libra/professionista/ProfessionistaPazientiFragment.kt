package it.polito.s279941.libra.professionista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionistapazienti.OnPatientItemClickListener
import it.polito.s279941.libra.professionistapazienti.PazientiAdapter
import it.polito.s279941.libra.professionistapazienti.PazientiItem
import it.polito.s279941.libra.professionistapazienti.ProfessionistaPazienteMainActivity
import kotlinx.android.synthetic.main.professionista_pazienti_fragment.*

class ProfessionistaPazientiFragment: Fragment(R.layout.professionista_pazienti_fragment), OnPatientItemClickListener {

    val patientViewModel by activityViewModels<ProfessionistaViewModel>()
    val patientAdapter = PazientiAdapter(this);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientViewModel.pazientiLiveData.observe(viewLifecycleOwner, Observer { data -> patientAdapter.setPazienti(data) })
        recyclerView_pazienti.layoutManager= LinearLayoutManager(requireContext())
        recyclerView_pazienti.adapter = patientAdapter

    }

    override fun onItemClick(item: PazientiItem, position: Int){
        //Toast.makeText(activity, item.nome_utente, Toast.LENGTH_SHORT).show()
        val intent = Intent (activity, ProfessionistaPazienteMainActivity::class.java)
        intent.putExtra("Patient", item.nome_utente)
        activity?.startActivity(intent)
    }

}