package it.polito.s279941.libra.professionista

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
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
    //lateinit var patientAdapter: PazientiAdapter
    val patientAdapter = PazientiAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patientViewModel.pazientiLiveData.observe(viewLifecycleOwner, Observer { data -> patientAdapter.setPazienti(data) })
        recyclerView_pazienti.layoutManager= LinearLayoutManager(requireContext())
        recyclerView_pazienti.adapter = patientAdapter

        setHasOptionsMenu(true)

        searchPatient.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                patientAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                patientAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.professionista_pazienti_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.ordine_alfabetico -> {
                patientAdapter.sortAlphabetically()
            }
            R.id.ordine_ultimo_controllo -> {
                patientAdapter.sortChronologically()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: PazientiItem, position: Int){
        //Toast.makeText(activity, item.nome_utente, Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, ProfessionistaPazienteMainActivity::class.java)
        intent.putExtra("Patient", item.nome_utente)

        // TODO: impostare l'id del paziente perchè serve alla vista di dettaglio paziente
        intent.putExtra("PatientId",item.nome_utente); // invece di item.nome_utente =>  item._id)
        activity?.startActivity(intent)
    }

}