package it.polito.s279941.libra.utente

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.utente_profilo_fragment.*

class UtenteProfiloFragment: Fragment(R.layout.utente_profilo_fragment) {

    private val utenteViewModel: UtenteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_name.text = utenteViewModel.utenteCorrente.nome
        //user_surname.text = utenteViewModel.utenteCorrente.cognome
    }
}