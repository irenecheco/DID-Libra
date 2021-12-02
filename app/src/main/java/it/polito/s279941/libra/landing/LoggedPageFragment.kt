package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_logged_page.*

/* Fragment di test per login */

class LoggedPageFragment : Fragment() {
    // creo il rif al viewModel istanziato dall'activity
    private val viewModel: LandingPageViewModel by activityViewModels()

    companion object {
        fun newInstance() = LoggedPageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logged_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val loginButton : Button = view.findViewById(R.id.loginButton)
        Log.d(LOG_TAG, "LoggedPageFragment called after a  SUCCESSFUL LOGIN") //--->DBG
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in LoggedPageFragment") //--->DBG

        // osservo attributo utenteCorrente del viewModel
        viewModel.utenteCorrente.observe(viewLifecycleOwner){
            // aggiorno la vista
            val alcuniAttributiUtenteCorrente = with(viewModel.utenteCorrente.value){
                "LOGGED USER: " +
                        this?.nome.toString() + " " +
                        this?.cognome.toString() + " - " +
                        this?.email.toString() + "\n" +
                        this?.tipo} //--->DBG
            Log.d(LOG_TAG, "LoggedPageFragment --> attributi utenteCorrente=" + alcuniAttributiUtenteCorrente) //--->DBG
            loggedUserTV.text = alcuniAttributiUtenteCorrente
        }

        Log.d(LOG_TAG, "end of onViewCreated()  in LoggedPageFragment")//--->DBG
    }
}

