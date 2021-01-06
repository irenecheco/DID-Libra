package it.polito.s279941.libra.utentedieta

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utente.UtenteViewModel
import kotlinx.android.synthetic.main.utente_dieta_calendario_fragment.view.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import java.text.DateFormat
import java.util.*

class DietaAppuntiFragment: Fragment(R.layout.utente_dieta_appunti_fragment) {
    // utenteViewModel by activityViewModels<UtenteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("aaaa","onViewCreated")
    }

}