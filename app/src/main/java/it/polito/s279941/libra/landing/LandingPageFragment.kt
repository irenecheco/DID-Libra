package it.polito.s279941.libra.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionista.ProfessionistaMainActivity
import it.polito.s279941.libra.utente.UtenteMainActivity
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_landing_page.*


/**
 * A simple [Fragment] subclass.
 * Use the [LandingPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingPageFragment : Fragment() {
    // istanzio il viewModel
    private val viewModel: LandingPageViewModel by activityViewModels()

    companion object {
        fun newInstance() = LandingPageFragment()
    }

    //private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Qui può essere eseguita la personalizzazione della GUI
    }


    // indica al frammento che è terminata la creazione dell'activity
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loginPageFragment = LoginPageFragment()

        Log.d(LOG_TAG, "onActivityCreated start in LandingPageFragment")
        Log.d(LOG_TAG, "  viewModel: " + viewModel.toString() + " in LandingPageFragment")

        utente_button.setOnClickListener{
            Log.d(LOG_TAG, "event CLICK on INTERFACCIA UTENTE button in LandingPageFragment")
            val i = Intent(activity, UtenteMainActivity::class.java)
            startActivityForResult(i, 1)
        }

        professionista_button.setOnClickListener{
            val i = Intent(activity, ProfessionistaMainActivity::class.java)
            startActivityForResult(i, 1)
        }

        preLoginButton.setOnClickListener{
            Log.d(LOG_TAG, "event CLICK on (pre)LOGIN button id: " + preLoginButton.id.toString() + " in LandingPageFragment")
            viewModel.selectButton(preLoginButton.id)
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.landing_page_fragment_container, loginPageFragment)
            transaction?.addToBackStack("LandigPageFragment")
            transaction?.commit()
        }
    }

}