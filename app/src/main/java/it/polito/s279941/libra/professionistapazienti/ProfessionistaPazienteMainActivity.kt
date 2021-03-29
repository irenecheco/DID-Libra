package it.polito.s279941.libra.professionistapazienti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_paziente_activity_main.*

class ProfessionistaPazienteMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.professionista_paziente_activity_main)
        setSupportActionBar(toolbar2)

        val paziente_profilo_fragment = ProfessionistaPazienteProfiloFragment()
        val paziente_dieta_fragment = ProfessionistaPazienteDietaFragment()
        val paziente_bilancia_fragment = ProfessionistaPazienteBilanciaFragment()
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
                R.id.patient_libra_page -> {
                    showFragment(paziente_bilancia_fragment)
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

