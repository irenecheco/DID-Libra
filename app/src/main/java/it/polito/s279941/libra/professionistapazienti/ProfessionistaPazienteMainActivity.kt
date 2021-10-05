package it.polito.s279941.libra.professionistapazienti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import it.polito.s279941.libra.databinding.ProfessionistaPazientiFragmentBinding
import it.polito.s279941.libra.professionista.ProfessionistaPazientiFragment
import kotlinx.android.synthetic.main.professionista_paziente_activity_main.*

class ProfessionistaPazienteMainActivity : AppCompatActivity() {

    lateinit var binding: ProfessionistaPazientiFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

