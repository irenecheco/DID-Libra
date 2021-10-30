package it.polito.s279941.libra.professionista

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_activity_main.*

class ProfessionistaMainActivity : AppCompatActivity() {

    // Riferimento al viewModel
    val proViewModel by viewModels<ProfessionistaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.professionista_activity_main)


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



        setSupportActionBar(toolbar_professionista)
        setUpTabs()
    }

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