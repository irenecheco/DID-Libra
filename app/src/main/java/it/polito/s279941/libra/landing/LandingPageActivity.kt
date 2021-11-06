package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.BACKEND_IP
import it.polito.s279941.libra.utils.LOG_TAG
import it.polito.s279941.libra.utils.MONGO_PORT
import java.io.IOException


class LandingPageActivity : AppCompatActivity() {
    // istanzio il viewModel per coordinare il passaggio dati tra i fragment dell'activity
    private val viewModel: LandingPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        if (savedInstanceState == null) {
            Toast.makeText(this,"Checking connection", Toast.LENGTH_SHORT).show()
            Log.d(LOG_TAG, "isConnected()=${isConnected()}  in LandingPageActivity")    //-->DBG
            if ( ! isConnected() ) {
                val networkErrorFragment = NetworkErrorFragment.newInstance()
                showFragment(networkErrorFragment)
            }
            else {
                val landingPageFragment = LandingPageFragment.newInstance()
                showFragment(landingPageFragment)
            }
        }
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in LandingPageActivity")    //-->DBG
    }

    fun showFragment(f: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.landing_page_fragment_container, f).commit()
        }
    }


    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        // usiamo sia ping che netcat perché su emulatore ping non passa e su cell reale manca netcat
        val command1 = "ping -c 1 -W 1 ${BACKEND_IP}"
        val command2 = "nc -v -w 1 ${BACKEND_IP} ${MONGO_PORT}"

        if (Runtime.getRuntime().exec(command1).waitFor() == 0){
            Log.d(LOG_TAG, "ping to ${BACKEND_IP} OK  in LandingPageActivity")  //-->DBG
            return true
        }
        else {
            Log.d(LOG_TAG, "try with netcat to ${BACKEND_IP}:${MONGO_PORT}  in LandingPageActivity")    //-->DBG
            return Runtime.getRuntime().exec(command2).waitFor() == 0
        }
    }



    fun quitApp() {
        this.finish()
        System.exit(0)
    }

}
