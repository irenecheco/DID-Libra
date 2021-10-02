package it.polito.s279941.libra.utente

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utente.UtenteMainActivity
import it.polito.s279941.libra.utente.UtenteProfiloFragment
import it.polito.s279941.libra.utente.UtenteViewModel
import it.polito.s279941.libra.utentedieta.DietaAppuntiFragment
import it.polito.s279941.libra.utentedieta.DietaCalendarioFragment
import it.polito.s279941.libra.utentedieta.PastoAdapter
import it.polito.s279941.libra.utentedieta.PastoItem
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import java.text.DateFormat
import java.util.*

class UtenteDietaFragment : Fragment(R.layout.utente_dieta_fragment) {

    val utenteViewModel by activityViewModels<UtenteViewModel>()
    val adapter = PastoAdapter();

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("aaaa","onViewCreated")
        //tv_today_data.text= DateFormat.getDateInstance(DateFormat.SHORT).format(Date(System.currentTimeMillis()))
        utenteViewModel.giornoLiveData.observe(viewLifecycleOwner,
            Observer { giorno -> tv_today_data.text= DateFormat.getDateInstance(DateFormat.SHORT).format(Date(giorno))
            })


        utenteViewModel.pastiDelGiornoLiveData.observe(viewLifecycleOwner,
            Observer { data -> adapter.setPastiDelGiorno(data)
            })

        utente_pasti_rv.layoutManager= LinearLayoutManager(requireContext())
        utente_pasti_rv.adapter = adapter


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.utente_dieta_menu,menu)
        super.onCreateOptionsMenu(menu,menuInflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.mnit_appunti -> {
                //findNavController().navigate(R.id.action_utenteDietaFragment2_to_dietaAppuntiFragment)
                val f: Fragment = DietaAppuntiFragment()
                activity?.supportFragmentManager?.beginTransaction()?.apply{
                    replace(R.id.fragment_container, f).addToBackStack("dietafrag_appunti").commit()
                }
            }
            R.id.mnit_calendario -> {
                //findNavController().navigate(R.id.action_utenteDietaFragment2_to_dietaCalendarioFragment)
                val f: Fragment = DietaCalendarioFragment()
                activity?.supportFragmentManager?.beginTransaction()?.apply{
                    replace(R.id.fragment_container, f).addToBackStack("dietafrag_calendar").commit()
                    //add(R.id.fragment_container, f).commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}