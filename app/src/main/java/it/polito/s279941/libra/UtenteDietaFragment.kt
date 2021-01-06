package it.polito.s279941.libra

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.utentedieta.PastoAdapter
import it.polito.s279941.libra.utentedieta.PastoItem
import kotlinx.android.synthetic.main.utente_dieta_fragment.*

class UtenteDietaFragment: Fragment(R.layout.utente_dieta_fragment) {
    val data = listOf(
        PastoItem("Colazione", "una mela", true),
        PastoItem("Spuntino Mattina", "yogurt", false)
    )

    val adapter = PastoAdapter(data);
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}