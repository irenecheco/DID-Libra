package it.polito.s279941.libra.professionistapazienti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.databinding.ProfessionistaPazientiFragmentBinding
import it.polito.s279941.libra.professionista.ProfessionistaPazientiFragment
import it.polito.s279941.libra.professionistadieta.ProfessionistaGiorniDietaPazienteViewModel
import kotlinx.android.synthetic.main.professionista_paziente_activity_main.*

class ProfessionistaPazienteMainActivity : AppCompatActivity() {

    val professionistaGiorniDietaPazienteViewModel by viewModels<ProfessionistaGiorniDietaPazienteViewModel>()

    lateinit var binding: ProfessionistaPazientiFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Sofia da verificare, non sono riuscita a provare a causa del network error e poi mi serve
        // un utente professionista ed un utente paziente gia inseriti
        //val idUtente = "61391a94f264961050bd82fb"
        val gson = Gson()
        val utenteCorrenteGson = intent.getStringExtra("libra.loggedUserGson")
        val utenteCorrente = gson.fromJson(utenteCorrenteGson, UtenteDataClass::class.java)
        professionistaGiorniDietaPazienteViewModel.setPaziente(utenteCorrente._id)



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

