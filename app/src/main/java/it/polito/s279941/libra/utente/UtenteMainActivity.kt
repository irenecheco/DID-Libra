package it.polito.s279941.libra.utente

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.DataModel.CalendarioDieta
import it.polito.s279941.libra.DataModel.Dieta
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utentedieta.PastoItem
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.utente_activity_main.*
import java.util.*

class UtenteMainActivity : AppCompatActivity() {

    val utenteViewModel by viewModels<UtenteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "UtenteMainActivity avviata")
        setContentView(R.layout.utente_activity_main)
        setSupportActionBar(toolbar)


        // TODO: Chi carica dal database l' UtenteDataClass deve passare l'istanza con il
        // metodo utenteViewModel.initByUtenteDataClass(utenteDataClassIstancCaricataDalDatabase)
        // Intando creo un MOCK vuoto dell' UtenteDataClass e temporaneamente gli passo il mock
        val utenteDataClassIstancCaricataDalDatabase = UtenteDataClass()
        //utenteViewModel.initByUtenteDataClass(utenteDataClassIstancCaricataDalDatabase)
        utenteViewModel.setPaziente("6071aea342e7530e8c1947ed")



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