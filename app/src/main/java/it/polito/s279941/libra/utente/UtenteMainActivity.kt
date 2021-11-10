package it.polito.s279941.libra.utente

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.utente_activity_main.*

class UtenteMainActivity : AppCompatActivity() {

    val utenteViewModel by viewModels<UtenteViewModel>()
    private var _obiettiviLista = MutableLiveData<List<Obiettivo>>()
    var obiettiviLista : LiveData<List<Obiettivo>> = _obiettiviLista

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
        //utenteViewModel.setPaziente("61391a94f264961050bd82fb")


        /** @AG  **/
        // estraggo dall'intent l'oggetto json passatto dal fragment precedente e lo riconverto
        // in oggetto UtenteDataClass
        val gson = Gson()
        val utenteCorrenteGson = intent.getStringExtra("libra.loggedUserGson")
        Log.d(LOG_TAG, "UtenteMainActivity :--> utenteCorrenteGson: ${utenteCorrenteGson}")
        utenteViewModel.utenteCorrente = gson.fromJson(utenteCorrenteGson, UtenteDataClass::class.java)
        Log.d(LOG_TAG, "UtenteMainActivity :--> utenteCorrente<${utenteViewModel.utenteCorrente.javaClass}>, ${utenteViewModel.utenteCorrente}")
        // classe init per test dato che l'altra andava in crash
        utenteViewModel.initByUtenteDataClass_AG(utenteViewModel.utenteCorrente)


        // OBIETTIVI
        // prendo la lista degli obiettivi dell'utente a partire dall'utente corrente in UtenteViewModel
        obiettiviLista = utenteViewModel.getGoalsFromUserData()


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