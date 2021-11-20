package it.polito.s279941.libra.professionista

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.landing.LandingPageViewModel
import it.polito.s279941.libra.landing.LoginRepository
import it.polito.s279941.libra.professionistapazienti.*
import it.polito.s279941.libra.utente.UtenteMainActivity
import it.polito.s279941.libra.utente.UtenteViewModel
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_pazienti_fragment.*

class ProfessionistaPazientiFragment: Fragment(R.layout.professionista_pazienti_fragment), OnPatientItemClickListener {

    val nutViewModel by activityViewModels<ProfessionistaViewModel>()
    val patientViewModel by activityViewModels<LandingPageViewModel>()
    val pazienteViewModel by viewModels<ProfessionistaPazienteViewModel>()
    //lateinit var patientAdapter: PazientiAdapter
    val patientAdapter = PazientiAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nutViewModel.pazientiLiveData.observe(viewLifecycleOwner, Observer { data -> patientAdapter.setPazienti(data) })
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

        patientViewModel.pazienteId.pazienteId = "617ea2a7b5bee74a7064f702"
        patientViewModel.findPaziente()

        //controllo su live data
        patientViewModel.pazienteCorrente.observe(viewLifecycleOwner) {

            val gson = Gson()
            //riempio pazienteCorrente in pazienteViewModel
            pazienteViewModel.pazienteCorrente = patientViewModel.pazienteCorrente.value!!
            val pazienteCorrenteInLivedata: UtenteDataClass = pazienteViewModel.pazienteCorrente
            /*val pazienteCorrenteInLivedata: UtenteDataClass? = patientViewModel.pazienteCorrente.value*/
            val pazienteCorrenteGson = gson.toJson(pazienteCorrenteInLivedata)
            Log.d(
                LOG_TAG,
                "pazienteViewModel.pazienteCorrente --> " + pazienteViewModel.pazienteCorrente
            )
            //Controllo che effettivamente pazienteCorrente sia pieno e che sia un paziente
            when (pazienteViewModel.getTipologiaUtente()) {
                "PAZ" -> {
                    val intent = Intent(activity, ProfessionistaPazienteMainActivity::class.java)
                    intent.putExtra("libra.pazienteGson", pazienteCorrenteGson)
                    startActivityForResult(intent, 1)
                    //activity?.startActivity(intent)
                }
                "NETERR" -> {
                    Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> NETERR") //--->DBG
                    Toast.makeText(this.context, R.string.nutr_patient_ErrorMsg, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

}