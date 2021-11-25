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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.landing.LandingPageViewModel
import it.polito.s279941.libra.professionistapazienti.*
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_pazienti_fragment.*


class ProfessionistaPazientiFragment: Fragment(R.layout.professionista_pazienti_fragment), OnPatientItemClickListener {

    val nutViewModel by activityViewModels<ProfessionistaViewModel>()
    val landingPageViewModel by activityViewModels<LandingPageViewModel>()
    val pazienteViewModel by viewModels<ProfessionistaPazienteViewModel>()
    val patientAdapter = PazientiAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nutViewModel.pazientiLista.observe(viewLifecycleOwner, Observer {
            pazienti -> patientAdapter.setPazienti(pazienti)
        })

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
                patientAdapter.sortAlphabeticallyWithName()
            }
            R.id.ordine_ultimo_controllo -> {
                patientAdapter.sortAlphabeticallyWithSurname()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: Paziente, position: Int){
        //landingPageViewModel.pazienteId.pazienteId = "619e274d328e957ddd522ce2"  // TODO da sistemare
        landingPageViewModel.pazienteId.pazienteId = item.IdPaziente
        landingPageViewModel.findPaziente()

        //controllo su live data
        landingPageViewModel.pazienteCorrente.observe(viewLifecycleOwner) {

            val gson = Gson()
            //riempio pazienteCorrente in pazienteViewModel
            pazienteViewModel.pazienteCorrente = landingPageViewModel.pazienteCorrente.value!!
            val pazienteCorrenteInLivedata: UtenteDataClass = pazienteViewModel.pazienteCorrente
            /*val pazienteCorrenteInLivedata: UtenteDataClass? = landingPageViewModel.pazienteCorrente.value*/
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