package it.polito.s279941.libra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.polito.s279941.libra.professionista.ProfessionistaMainActivity
import it.polito.s279941.libra.utente.UtenteMainActivity
import kotlinx.android.synthetic.main.activity_landing_page.*

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        utente_button.setOnClickListener{
            val i = Intent(this, UtenteMainActivity::class.java)
            startActivityForResult(i, 1)
        }

        professionista_button.setOnClickListener{
            val i = Intent(this, ProfessionistaMainActivity::class.java)
            startActivityForResult(i, 1)
        }
    }
}