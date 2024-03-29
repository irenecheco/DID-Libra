package it.polito.s279941.libra.landing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionista.ProfessionistaMainActivity
import it.polito.s279941.libra.utente.UtenteMainActivity
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_login_page.*

class LoginPageFragment : Fragment() {
    // creo il rif al viewModel istanziato dall'activity
    private val viewModel: LandingPageViewModel by activityViewModels()

    // flag per verifica integrità dati inseriti
    private var emailFieldDataIntegrity : Boolean = false
    private var passwordFieldDataIntegrity : Boolean = false

    companion object {
        fun newInstance() = LoginPageFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val loginButton : Button = view.findViewById(R.id.loginButton)
        Log.d(LOG_TAG, "LoginPageFragment called by pressing button with id : " + viewModel.getSelectedButtonId() + " in LandingPageFragment") //--->DBG
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in LoginPageFragment") //--->DBG
        // bottone LOGIN disabilitato finché non sono inseriti i dati corretti
        loginButton.isEnabled = emailFieldDataIntegrity && passwordFieldDataIntegrity

        val landingPageFragment = LandingPageFragment.newInstance() //--->DBG

        // Verifica integrità dati
        emailField.doAfterTextChanged() {
        // regex con match ~99,9% https://emailregex.com/
            val email_regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
            if (emailField.text.matches(Regex(email_regex))) {
                Log.d(LOG_TAG, "email field matches regex: ${emailField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteLoginData.email=emailField.text.toString()
                emailFieldDataIntegrity = true
                emailLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                emailLabel.text = emailLabel.text.trimStart('*',' ')
            } else{
                Log.d(LOG_TAG, "email field does NOT match regex: ${emailField.text}") //--->DBG
                emailFieldDataIntegrity = false
                emailLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                emailLabel.text = emailLabel.text.trimStart('*',' ')
                emailLabel.text = "* ${emailLabel.text}"
            }
            loginButton.isEnabled = emailFieldDataIntegrity && passwordFieldDataIntegrity
        }
        // Verifica integrità dati
        passwordField.doAfterTextChanged() {
            // blando check su campo mail (almeno 3 caratteri)
            if (passwordField.text.matches(Regex(".{3,}"))) {
                Log.d(LOG_TAG, "password field matches regex: ${passwordField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteLoginData.password = passwordField.text.toString()
                passwordFieldDataIntegrity = true
                // NON ha senso informare della lunghezza password in questo contesto
            } else {
                Log.d(LOG_TAG, "password field does NOT match regex: ${passwordField.text}") //--->DBG
                passwordFieldDataIntegrity = false
                // NON ha senso informare della lunghezza password in questo contesto
            }
            loginButton.isEnabled = emailFieldDataIntegrity && passwordFieldDataIntegrity
        }

        loginButton.setOnClickListener {
            Log.d(LOG_TAG, "CLICK event on LOGIN button id: " + loginButton.id.toString() + " in LoginPageFragment") //--->DBG
            viewModel.logLogingData() //--->DBG
            viewModel.login()
            loginButton.visibility = View.INVISIBLE
            progressBarLogin.visibility = View.VISIBLE
            // Osservo il LiveData "utenteCorrente" e quando aggiornato invoco la transizione
            // alla pagina coretta
            viewModel.utenteCorrente.observe(viewLifecycleOwner) {
                progressBarLogin.visibility = View.INVISIBLE
                loginButton.visibility = View.VISIBLE

                // Estraggo l'oggetto di tipo UtenteDataClass contenuto nel Livedata per trasformarlo in
                // oggetto json e poterlo passare all'activity successiva
                val gson = Gson()
                val utenteCorrenteInLivedata : UtenteDataClass? = viewModel.utenteCorrente.value
                val utenteCorrenteGson = gson.toJson(utenteCorrenteInLivedata)
                //Log.d(LOG_TAG, "utenteCorrenteGson: " + utenteCorrenteGson + " in LoginPageFragment") //--->DBG

                //Log.d(LOG_TAG, "before 'when (viewModel.getTipologiaUtente())'   in LoginPageFragment") //--->DBG
                /* La notifica di eventuali errori viene fatta per semplicità tramite Toast, che fa apparire
                * a schermo una sorta di pop-up con il messaggio */
                when (viewModel.getTipologiaUtente()) {
                    "PAZ" -> {
                        Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> PAZ") //--->DBG
                        val myIntent = Intent(activity, UtenteMainActivity::class.java)
                        myIntent.putExtra("libra.loggedUserGson", utenteCorrenteGson)
                        startActivityForResult(myIntent, 1)
                    }
                    "NUT" -> {
                        Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> NUT") //--->DBG
                        val myIntent = Intent(activity, ProfessionistaMainActivity::class.java)
                        myIntent.putExtra("libra.loggedUserGson", utenteCorrenteGson)
                        startActivityForResult(myIntent, 1)
                    }
                    /* se abilito queste due clausole (401 e 404) posso inviare un msg più dettagliato
                    * oltre a quello che viene inviato da NETERR
                    */
                    /*"401" -> {
                        Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> 401") //--->DBG
                        Toast.makeText(this.context, "utente inesistente", Toast.LENGTH_LONG).show()
                    }
                    "404" -> {
                        Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> 404") //--->DBG
                        Toast.makeText(this.context, "utente inesistente", Toast.LENGTH_LONG).show()
                    }*/
                    // Mandiamo alll'utente un generico messaggio di errore per il login
                    // e torniamo alla landing page
                    "NETERR" -> {
                        Log.d(LOG_TAG, "when (viewModel.getTipologiaUtente() -> NETERR") //--->DBG
                        Toast.makeText(this.context, R.string.loginErrorMsg, Toast.LENGTH_LONG).show()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.landing_page_fragment_container, landingPageFragment)
                        transaction?.addToBackStack("LoginPageFragment")
                        transaction?.commit()
                    }
                }
            }
        }
    }
}

