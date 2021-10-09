package it.polito.s279941.libra.professionistadieta

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_giorno_dieta_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_appunti_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import java.text.DateFormat
import java.util.*

class ProfessionistaGiornoDietaFragment : Fragment(R.layout.professionista_giorno_dieta_fragment) {

    val professionistaGiorniDietaPazienteViewModel by activityViewModels<ProfessionistaGiorniDietaPazienteViewModel>()
    val adapter = PastoAdapter();


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("aaaa","onViewCreated")
       // tv_today_data.text= DateFormat.getDateInstance(DateFormat.SHORT).format(Date(System.currentTimeMillis()))
       // professionistaDietaViewModel.giornoLiveData.observe(viewLifecycleOwner,
       //     Observer { giorno -> tv_today_data.text= DateFormat.getDateInstance(DateFormat.SHORT).format(Date(giorno))
       //     })

        salvaGiornoButton.setOnClickListener {
            val pasti = adapter.getPastiDelGiorno()
            professionistaGiorniDietaPazienteViewModel.updateDatiGiornoInModifica(
                pasti[0].descrizione, pasti[1].descrizione, pasti[2].descrizione,
                pasti[3].descrizione, pasti[4].descrizione
            )

            this.activity?.onBackPressed()

        }


        professionistaGiorniDietaPazienteViewModel.giornoInModificaLiveData.observe(viewLifecycleOwner,
            Observer { data -> adapter.setPastiDelGiorno(data)
            })

         professionista_pasti_rv.layoutManager= LinearLayoutManager(requireContext())
         professionista_pasti_rv.adapter = adapter
        // setHasOptionsMenu(true)
    }


}