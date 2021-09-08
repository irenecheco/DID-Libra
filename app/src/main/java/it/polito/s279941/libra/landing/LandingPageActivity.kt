package it.polito.s279941.libra.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R

/*
class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.landing_page_fragment_container, LandingPageFragment.newInstance())
                .commitNow()
        }
    }
}
*/

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        if (savedInstanceState == null) {
            val landing_page_fragment = LandingPageFragment.newInstance()
            showFragment(landing_page_fragment)
        }
    }


    fun showFragment(f: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.landing_page_fragment_container, f).commit()
        }
    }

}
