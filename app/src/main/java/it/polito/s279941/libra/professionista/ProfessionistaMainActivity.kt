package it.polito.s279941.libra.professionista

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_activity_main.*

class ProfessionistaMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.professionista_activity_main)

        setSupportActionBar(toolbar_professionista)
        setUpTabs()
    }

    private fun setUpTabs(){
        val profilo: String = getString(R.string.tab_profile_label)
        val pazienti: String = getString(R.string.tab_patients_label)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ProfessionisaProfiloFragment(), profilo)
        adapter.addFragment(ProfessionistaPazientiFragment(), pazienti)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


    }
}