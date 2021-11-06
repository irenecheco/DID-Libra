package it.polito.s279941.libra.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.UtenteDataClass
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.DataModel.UtenteSigninData
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utils.LOG_TAG

// https://thanasakis.medium.com/restful-api-consuming-on-android-using-retrofit-and-architecture-components-livedata-room-and-59e3b064f94
// https://developer.android.com/jetpack/guide#connect-viewmodel-repository

// il viewModel si occupa della gestione dei dati
class LandingPageViewModel : ViewModel(){
    private var selectedButtonId : Int = 0

    // istanzio oggetti per il recupero dei dati
    val restApiManager = RestApiManager()
    val loginRepository = LoginRepository(restApiManager)
    val signinRepository = SigninRepository(restApiManager)

    // serve a contenere le credenziali che verranno passate
    // dall'utente nella finestra di login (LoginPageFragment)
    var utenteLoginData: UtenteLoginData = UtenteLoginData()

    // serve a contenere le info passate in fase di registrazione
    var utenteSigninData: UtenteSigninData = UtenteSigninData()

    // contiene tutte le info sull'utente che ha fatto il login
    // (non so come mai c'è il passaggio intermedio attraverso la var
    // che inizia con "_" ma su sito android era così
    // definiti come LiveData al fine di essere osservabili dal Fragment
    private var _utenteCorrente = MutableLiveData<UtenteDataClass>()
    var utenteCorrente : LiveData<UtenteDataClass> = _utenteCorrente

    // var per comunicazione msg di errore
    var netErrorMsg = ""


    // invoca il repository per il recupero dei dati
    fun login(){
        Log.d(LOG_TAG, "  start login()  in LandingPageViewModel") //--->DBG
        _utenteCorrente = loginRepository.login(utenteLoginData)
        utenteCorrente = _utenteCorrente
        Log.d(LOG_TAG, "LandingPageViewModel --> _utenteCorrente=" + _utenteCorrente.toString()) //--->DBG
        Log.d(LOG_TAG, "LandingPageViewModel --> utenteCorrente=" + utenteCorrente.toString()) //--->DBG
        val alcuniAttributiUtenteCorrente = with(utenteCorrente.value){
            "nome " + this?.nome.toString() + " " +
            "cogn " + this?.cognome.toString() + " - " +
            "email " + this?.email.toString() + " " +
            "tipo " + this?.tipo} //--->DBG
        Log.d(LOG_TAG, "LandingPageViewModel --> attributi utenteCorrente=" + alcuniAttributiUtenteCorrente) //--->DBG
    }

    // invoca il repository per la registrazione
    fun signin(){
        Log.d(LOG_TAG, "  start sigin()  in LandingPageViewModel") //--->DBG
        Log.d(LOG_TAG,"   dati inseriti: \n" +
                "Tipo=${utenteSigninData.tipo},\n" +
                "Data nascita=${utenteSigninData.data_nascita},\n" +
                "Nome=${utenteSigninData.nome},\n" +
                "Cognome=${utenteSigninData.cognome},\n" +
                "email=${utenteSigninData.email},\n" +
                "cod_nutriz=${utenteSigninData.cod_nutrizionista},\n" +
                "password=${utenteSigninData.password},\n" +
                "data iscrizione=${utenteSigninData.data_iscrizione}")

        _utenteCorrente = signinRepository.signin(utenteSigninData)
        utenteCorrente = _utenteCorrente

        val alcuniAttributiUtenteRegistrato = with(utenteCorrente.value){
            "nome " + this?.nome.toString() + " " +
                    "cogn " + this?.cognome.toString() + " - " +
                    "email " + this?.email.toString() + " " +
                    "tipo " + this?.tipo
                    "_id " + this?._id } //--->DBG
        Log.d(LOG_TAG, "LandingPageViewModel --> attributi utenteRegistrato=" + alcuniAttributiUtenteRegistrato) //--->DBG
    }


    // se restituisce "NETERR" c'è stato un errore o il valore .tipo, che viene assegnato in RestApiManager
    fun getTipologiaUtente(): String {
        // se .tipo è null assegno valore 'NETERR' come defalut
        //Log.d(LOG_TAG, "1 getTipologiaUtente()=${utenteCorrente.value?.tipo}  in LandingPageViewModel") //--->DBG
        val t = utenteCorrente.value?.tipo ?: "NETERR"
        //Log.d(LOG_TAG, "  2 getTipologiaUtente()=${utenteCorrente.value?.tipo}  in LandingPageViewModel")
        return t
    }

    // varie funzioni a scopo di debug
    fun setSelectedButton (buttonId: Int) {
        Log.d(LOG_TAG, "selectButton() id : " + buttonId + " in LandingPageViewModel") //--->DBG
        selectedButtonId = buttonId
    }

    fun getSelectedButtonId (): Int {
        Log.d(LOG_TAG, "selectedButton() id : " + selectedButtonId + " in LandingPageViewModel") //--->DBG
        return selectedButtonId
    }

    fun logLogingData() {
        Log.d(LOG_TAG, "utenteLoginData: email= ${utenteLoginData.email}  in LandingPageViewModel") //--->DBG
        Log.d(LOG_TAG, "utenteLoginData: password= ${utenteLoginData.password}  in LandingPageViewModel") //--->DBG
    }

    fun logSigninData() {
        Log.d(LOG_TAG,"landingPageViewModel -> dati inseriti: " +
                "Tipo=${utenteSigninData.tipo},\n" +
                "Data nascita=${utenteSigninData.data_nascita},\n" +
                "Nome=${utenteSigninData.nome},\n" +
                "Cognome=${utenteSigninData.cognome},\n" +
                "emain=${utenteSigninData.email},\n" +
                "password=${utenteSigninData.password}")
    }

}