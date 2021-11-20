package it.polito.s279941.libra.professionistapazienti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Peso
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.databinding.ProfessionistaPazientiFragmentBinding
import it.polito.s279941.libra.professionista.ProfessionistaPazientiFragment
import it.polito.s279941.libra.professionistadieta.ProfessionistaGiorniDietaPazienteViewModel
import it.polito.s279941.libra.utente.UtenteViewModel
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_paziente_activity_main.*

class ProfessionistaPazienteMainActivity : AppCompatActivity() {

    val professionistaGiorniDietaPazienteViewModel by viewModels<ProfessionistaGiorniDietaPazienteViewModel>()

    lateinit var binding: ProfessionistaPazientiFragmentBinding

    val pazienteViewModel by viewModels<ProfessionistaPazienteViewModel>()
    private var _obiettiviLista = MutableLiveData<List<Obiettivo>>()
    var obiettiviLista : LiveData<List<Obiettivo>> = _obiettiviLista
    private var _pesiLista = MutableLiveData<List<Peso>>()
    var pesiLista : LiveData<List<Peso>> = _pesiLista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** @AG  **/
        // estraggo dall'intent l'oggetto json passatto dal fragment precedente e lo riconverto
        // in oggetto UtenteDataClass
        val gson = Gson()
        val pazienteCorrenteGson = intent.getStringExtra("libra.pazienteGson")
        Log.d(LOG_TAG, "ProfessionistaPazienteMainActivity :--> pazienteCorrenteGson: ${pazienteCorrenteGson}")
        pazienteViewModel.pazienteCorrente = gson.fromJson(pazienteCorrenteGson, UtenteDataClass::class.java)
        Log.d(LOG_TAG, "ProfessionistaPazienteMainActivity :--> " +
                "pazienteCorrente<${pazienteViewModel.pazienteCorrente.javaClass}>, ${pazienteViewModel.pazienteCorrente}")

        // TODO: Sofia da verificare, non sono riuscita a provare a causa del network error e poi mi serve
        // un utente professionista ed un utente paziente gia inseriti
        // I seguenti si possono riattivare solo dopo che la lista di pazienti passa l'id del paxziente
        // val idUtente = intent.getStringExtra("PatientId") // da scommentare appena passato il vero id
        val idUtente = pazienteViewModel.pazienteCorrente._id // lo sovrascrivo perchè ancora non viene passato  l'id, da commentare appena passato il vero id
        professionistaGiorniDietaPazienteViewModel.setPaziente(idUtente!!)

        // MISURAZIONI PESO PER GRAFICO
        // prendo la lista delle misurazioni dei pesi dell'utente a partire dall'utente corrente in UtenteViewModel
        pesiLista = pazienteViewModel.getWeightFromUserData()


        // OBIETTIVI
        // prendo la lista degli obiettivi dell'utente a partire dall'utente corrente in UtenteViewModel
        obiettiviLista = pazienteViewModel.getGoalsFromUserData()

        //binding = DataBindingUtil.setContentView(this, R.layout.professionista_paziente_activity_main)
        setContentView(R.layout.professionista_paziente_activity_main)
        setSupportActionBar(toolbar2)

        toolbar2.title = intent.getStringExtra("Patient")

        val paziente_profilo_fragment = ProfessionistaPazienteProfiloFragment()
        val paziente_dieta_fragment = ProfessionistaPazienteDietaFragment()
        val paziente_chat_fragment = ProfessionistaPazienteChatFragment()

        paziente_bottom_bar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.patient_profile_page -> {
                    showFragment(paziente_profilo_fragment)
                    true
                }
                R.id.patient_diet_page -> {
                    showFragment(paziente_dieta_fragment)
                    true
                }
                R.id.patient_chat_page -> {
                    showFragment(paziente_chat_fragment)
                    true
                }
                else -> false
            }
        }

    }


    fun showFragment(f: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.pazienti_fragment_container, f).commit()
        }
    }
}

