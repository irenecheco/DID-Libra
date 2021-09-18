package it.polito.s279941.libra.landing

import android.util.Log
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.utils.LOG_TAG

class LandingPageViewModel : ViewModel(){
    private var selectedButtonId : Int = 0

    fun selectButton (buttonId: Int) {
        Log.d(LOG_TAG, "selectButton() id : " + buttonId + " in LandingPageViewModel")
        selectedButtonId = buttonId
    }

    fun selectedButtonId (): Int {
        Log.d(LOG_TAG, "selectedButton() id : " + selectedButtonId + " in LandingPageViewModel")
        return selectedButtonId
    }
}