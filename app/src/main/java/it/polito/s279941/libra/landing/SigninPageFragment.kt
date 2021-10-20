package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_signin_page.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SigninPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SigninPageFragment : Fragment() {
    // creo il rif al viewModel istanziato dall'activity
    private val viewModel: LandingPageViewModel by activityViewModels()

    // flag per verifica integrità dati inseriti
    private var nameFieldDataIntegrity : Boolean = false
    private var surnameFieldDataIntegrity : Boolean = false
    private var emailFieldDataIntegrity : Boolean = false
    private var passwordFieldDataIntegrity : Boolean = false
    private var codiceAlboFieldDataIntegrity : Boolean = false

    companion object {
        /* Use this factory method to create a new instance of this fragment */
        @JvmStatic
        fun newInstance() = SigninPageFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(LOG_TAG, "SigninPageFragment called by pressing button with id : " + viewModel.getSelectedButtonId() + " in LandingPageFragment") //--->DBG
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in SigninPageFragment") //--->DBG

        // bottone SIGNIN disabilitato finché non sono inseriti i dati corretti
        signinButton.isEnabled =
            nameFieldDataIntegrity &&
                    surnameFieldDataIntegrity &&
                    emailFieldDataIntegrity &&
                    passwordFieldDataIntegrity &&
                    codiceAlboFieldDataIntegrity


        // Imposto il form in base alla tipologia di utente (PAZ/NUT)
        signinRadioButtonPaz
            .setOnClickListener(View.OnClickListener {
                Log.d(LOG_TAG, "SigninPageFragment -> signinRadioButtonPAZ selected" + view.getId()) //--->DBG
                signinUserRoleBasedLabel.text = getString(R.string.signinUserRoleBasedLabelPaz)
                signinUserRoleBasedField.hint = getString(R.string.signinUserRoleBasedFieldPazTip)
                // salvo il valore nei dati di signin
                viewModel.utenteSigninData.tipo = "PAZ"
            })
        signinRadioButtonNut
            .setOnClickListener(View.OnClickListener {
                Log.d(LOG_TAG, "SigninPageFragment -> signinRadioButtonNUT selected" + view.getId()) //--->DBG
                signinUserRoleBasedLabel.text = getString(R.string.signinUserRoleBasedLabelNut)
                signinUserRoleBasedField.hint = getString(R.string.signinUserRoleBasedFieldNutTip)
                // salvo il valore nei dati di signin
                viewModel.utenteSigninData.tipo = "NUT"
        })

        // setto datePicker a data corrente
        val oggi = Calendar.getInstance()
        // imposto la massima data selezionabile a oggi
        datePickerBirthday.maxDate = oggi.timeInMillis
        //inizializzo a oggi il date picker
        datePickerBirthday.init(
            oggi.get(Calendar.YEAR),
            oggi.get(Calendar.MONTH),
            oggi.get(Calendar.DAY_OF_MONTH))
        { _, anno, mese, giorno ->
            val mese = mese + 1
            val msg = "You Selected: $giorno/$mese/$anno"
            //Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
            Log.d(LOG_TAG, "dayOfB datePicker: ${msg}")  //--->DBG
            var dayOfB = Calendar.getInstance()
            dayOfB.set(anno, mese, giorno)
            // cast al tipo supportato nel DB
            viewModel.utenteSigninData.data_nascita = dayOfB.time
        }

        // Verifica integrità dati nome
        nameField.doAfterTextChanged() {
            // regex per parole multiple
            val name_regex = "([A-Z][a-z]*[àèèìòù]?\\s?)+"
            if (nameField.text.matches(Regex(name_regex))) {
                Log.d(LOG_TAG, "name field matches regex: ${nameField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteSigninData.nome = nameField.text.toString()
                nameFieldDataIntegrity = true
                // ripristino stile per dato corretto
                signinNameLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinNameLabel.text = signinNameLabel.text.trimStart('*')
            } else{
                Log.d(LOG_TAG, "name field does NOT match regex: ${nameField.text}") //--->DBG
                nameFieldDataIntegrity = false
                // aggiungo "*" e cambio colore
                signinNameLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinNameLabel.text = signinNameLabel.text.trimStart('*')
                signinNameLabel.text = "* ${signinNameLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }
        // Verifica integrità dati cognome
        surnameField.doAfterTextChanged() {
            // regex per parole multiple
            val surname_regex = "([A-Z][a-z]*[àèèìòù]?\\s?)+"
            if (surnameField.text.matches(Regex(surname_regex))) {
                Log.d(LOG_TAG, "name field matches regex: ${surnameField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteSigninData.nome = surnameField.text.toString()
                surnameFieldDataIntegrity = true
                signinSurnameLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinSurnameLabel.text = signinSurnameLabel.text.trimStart('*')
            } else{
                Log.d(LOG_TAG, "email field does NOT match regex: ${surnameField.text}") //--->DBG
                surnameFieldDataIntegrity = false
                signinSurnameLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinSurnameLabel.text = signinSurnameLabel.text.trimStart('*')
                signinSurnameLabel.text = "* ${signinSurnameLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }
        // Verifica integrità dati email
        signinEmailField.doAfterTextChanged() {
            // regex con match ~99,9% https://emailregex.com/
            val email_regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
            if (signinEmailField.text.matches(Regex(email_regex))) {
                Log.d(LOG_TAG, "email field matches regex: ${signinEmailField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteSigninData.email = signinEmailField.text.toString()
                emailFieldDataIntegrity = true
                signinEmailLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinEmailLabel.text = signinEmailLabel.text.trimStart('*')
            } else{
                Log.d(LOG_TAG, "email field does NOT match regex: ${signinEmailField.text}") //--->DBG
                emailFieldDataIntegrity = false
                signinEmailLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinEmailLabel.text = signinEmailLabel.text.trimStart('*')
                signinEmailLabel.text = "* ${signinEmailLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }
        // Verifica integrità dati password
        val password_regex = ".{3,}" // // blando check su campo password (almeno 3 caratteri)
        signinPasswordField.doAfterTextChanged() {
            // blando check su campo passwd (almeno 3 caratteri)
            if (signinPasswordField.text.matches(Regex(password_regex))) {
                Log.d(LOG_TAG, "password field matches regex: ${signinPasswordField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteSigninData.password = signinPasswordField.text.toString()
                passwordFieldDataIntegrity = true
                signinPasswordLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinPasswordLabel.text = signinPasswordLabel.text.trimStart('*')
            } else {
                Log.d(LOG_TAG, "password field does NOT match regex: ${signinPasswordField.text}") //--->DBG
                passwordFieldDataIntegrity = false
                signinPasswordLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinPasswordLabel.text = signinPasswordLabel.text.trimStart('*')
                signinPasswordLabel.text = "* ${signinPasswordLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }
        // Verifica integrità dati conferma password
        signinPasswordConfirmField.doAfterTextChanged() {
            // verifica corrispondenza
            if (signinPasswordField.text.matches(Regex(password_regex)) && signinPasswordConfirmField.text.toString() == signinPasswordField.text.toString()) {
                Log.d(LOG_TAG, "passwordConfirm field matches password: ${signinPasswordConfirmField.text}") //--->DBG
                Toast.makeText(this.context, "password match", Toast.LENGTH_SHORT).show()
                // salvo il valore nel viewModel
                passwordFieldDataIntegrity = true
                signinPasswordConfirmLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinPasswordConfirmLabel.text = signinPasswordConfirmLabel.text.trimStart('*')
            } else {
                Log.d(LOG_TAG, "passwordConfirm field does NOT match password: ${signinPasswordConfirmField.text}") //--->DBG
                passwordFieldDataIntegrity = false
                signinPasswordConfirmLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinPasswordConfirmLabel.text = signinPasswordConfirmLabel.text.trimStart('*')
                signinPasswordConfirmLabel.text = "* ${signinPasswordConfirmLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }
        // Verifica integrità dati codice albo
        signinUserRoleBasedField.doAfterTextChanged() {
            // https://www.onb.it/servizi/elenco-iscritti/
            val albo_regex = "^[A-Z]{2}_\\d{6}"
            if (signinUserRoleBasedField.text.matches(Regex(albo_regex))) {
                Log.d(LOG_TAG, "email field matches regex: ${signinUserRoleBasedField.text}") //--->DBG
                // salvo il valore nel viewModel
                viewModel.utenteSigninData.cod_nutrizionista = signinUserRoleBasedField.text.toString()
                codiceAlboFieldDataIntegrity = true
                signinUserRoleBasedLabel.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                signinUserRoleBasedLabel.text = signinUserRoleBasedLabel.text.trimStart('*')
            } else{
                Log.d(LOG_TAG, "email field does NOT match regex: ${signinUserRoleBasedField.text}") //--->DBG
                codiceAlboFieldDataIntegrity = false
                signinUserRoleBasedLabel.setTextColor(resources.getColor(R.color.colorAccentERROR))
                signinUserRoleBasedLabel.text = signinUserRoleBasedLabel.text.trimStart('*')
                signinUserRoleBasedLabel.text = "* ${signinUserRoleBasedLabel.text}"
            }
            signinButton.isEnabled =
                nameFieldDataIntegrity &&
                        surnameFieldDataIntegrity &&
                        emailFieldDataIntegrity &&
                        passwordFieldDataIntegrity &&
                        codiceAlboFieldDataIntegrity
        }

        signinButton.setOnClickListener {
            Log.d(LOG_TAG, "CLICK event on SIGNIN button id: " + signinButton.id.toString() + " in SigninPageFragment") //--->DBG

        }

    }

}