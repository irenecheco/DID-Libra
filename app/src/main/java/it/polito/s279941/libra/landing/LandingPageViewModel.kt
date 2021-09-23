package it.polito.s279941.libra.landing

import android.util.Log
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.DataModel.UtenteLoginData
import it.polito.s279941.libra.api.RestApiManager
import it.polito.s279941.libra.utils.LOG_TAG

// il viewModel si occupa della gestione dei dati
class LandingPageViewModel : ViewModel(){
    private var selectedButtonId : Int = 0
    var utenteLoginData : UtenteLoginData = UtenteLoginData()

    fun setSelectedButton (buttonId: Int) {
        Log.d(LOG_TAG, "selectButton() id : " + buttonId + " in LandingPageViewModel")
        selectedButtonId = buttonId
    }

    fun getSelectedButtonId (): Int {
        Log.d(LOG_TAG, "selectedButton() id : " + selectedButtonId + " in LandingPageViewModel")
        return selectedButtonId
    }

    fun logLogingData() {
        Log.d(LOG_TAG, "utenteLoginData: email= ${utenteLoginData.email}  in LandingPageViewModel")
        Log.d(LOG_TAG, "utenteLoginData: password= ${utenteLoginData.password}  in LandingPageViewModel")
    }

    fun login(){
        val restApiManager = RestApiManager()
        restApiManager.login(utenteLoginData)
    }
}