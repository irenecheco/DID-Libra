package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG

class LandingPageActivity : AppCompatActivity() {
    // istanzio il viewModel per coordinare il passaggio dati tra i fragment dell'activity
    private val viewModel: LandingPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        if (savedInstanceState == null) {
            val landing_page_fragment = LandingPageFragment.newInstance()
            showFragment(landing_page_fragment)
        }
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in LandingPageActivity")
    }

    fun showFragment(f: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.landing_page_fragment_container, f).commit()
        }
    }

}
