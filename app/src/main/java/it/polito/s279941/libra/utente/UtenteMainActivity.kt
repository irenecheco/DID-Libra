package it.polito.s279941.libra.utente

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.utente_activity_main.*

class UtenteMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "UtenteMainActivity avviata")
        setContentView(R.layout.utente_activity_main)
        setSupportActionBar(toolbar)

        val profilo_fragment = UtenteProfiloFragment()
        val dieta_fragment = UtenteDietaFragment()
        val bilancia_fragment = UtenteBilanciaFragment()
        val storico_fragment = UtenteStoricoFragment()
        val chat_fragment = UtenteChatFragment()

        bottom_bar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.profile_page -> {
                    showFragment(profilo_fragment)
                    true
                }
                R.id.diet_page -> {
                    showFragment(dieta_fragment)
                    true
                }
                R.id.libra_page -> {
                    showFragment(bilancia_fragment)
                    true
                }
                R.id.history_page -> {
                    showFragment(storico_fragment)
                    true
                }
                R.id.chat_page -> {
                    showFragment(chat_fragment)
                    true
                }
                else -> false
            }
        }

    }


    fun showFragment(f: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container, f).commit()
        }
    }
}