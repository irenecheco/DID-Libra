package it.polito.s279941.libra.professionista

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_activity_main.*

class ProfessionistaMainActivity : AppCompatActivity() {

    // Riferimento al viewModel
    val proViewModel by viewModels<ProfessionistaViewModel>()
    private var _pazientiLista = MutableLiveData<MutableList<Paziente>>()
    var pazientiLista : LiveData<MutableList<Paziente>> = _pazientiLista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.professionista_activity_main)
        setSupportActionBar(toolbar_professionista)
        setUpTabs()

        /** @AG  **/
        // estraggo dall'intent l'oggetto json passatto dal fragment precedente e lo riconverto
        // in oggetto UtenteDataClass
        val gson = Gson()
        val utenteCorrenteGson = intent.getStringExtra("libra.loggedUserGson")
        Log.d(LOG_TAG, "ProfessionistaMainActivity :--> utenteCorrenteGson: ${utenteCorrenteGson}")
        proViewModel.utenteCorrente = gson.fromJson(utenteCorrenteGson, UtenteDataClass::class.java)
        Log.d(LOG_TAG, "ProfessionistaMainActivity :--> utenteCorrente<${proViewModel.utenteCorrente.javaClass}>, ${proViewModel.utenteCorrente}")
        // classe init per test dato che l'altra andava in crash
        proViewModel.initByUtenteDataClass_AG(proViewModel.utenteCorrente)


        // LISTA PAZIENTI
        // prendo la lista degli degli id dei pazienti del nutrizionista dall'utente corrente in ProfessionistaViewModel
        pazientiLista = proViewModel.getIdPatientFromUserData()
    }

    // set up del tab in alto nell'activity del professionista (profilo - pazienti)
    private fun setUpTabs(){
        val profilo: String = getString(R.string.tab_profile_label)
        val pazienti: String = getString(R.string.tab_patients_label)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ProfessionisaProfiloFragment(), profilo)
        adapter.addFragment(ProfessionistaPazientiFragment(), pazienti)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}