package it.polito.s279941.libra.professionistapazienti

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
import it.polito.s279941.libra.professionistadieta.*
import it.polito.s279941.libra.utentedieta.DietaAppuntiFragment
import it.polito.s279941.libra.utentedieta.DietaCalendarioFragment
import kotlinx.android.synthetic.main.professionista_paziente_dieta_fragment.*

//import java.util.*

class ProfessionistaPazienteDietaFragment: Fragment(R.layout.professionista_paziente_dieta_fragment),
    OnGiornoDietaRowButtonClickListener {
    val professionistaGiorniDietaPazienteViewModel by activityViewModels<ProfessionistaGiorniDietaPazienteViewModel>()
    val giornoAdapter = GiornoAdapter(this);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("aaaa","onViewCreated")

        professionistaGiorniDietaPazienteViewModel.giorniLiveData.observe(viewLifecycleOwner, Observer {
                data -> giornoAdapter.setGiorniDieta(data)
        })
        recyclerViewGiorni.layoutManager= LinearLayoutManager(requireContext())
        recyclerViewGiorni.adapter = giornoAdapter


        addGiornoDietaPaziente.setOnClickListener { view ->
            Log.d("aaaaPiuBtn","onClick")
            professionistaGiorniDietaPazienteViewModel.addGiorno()
        }

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


    override fun onEditaDietaGiorno(item: GiornoItem, position: Int) {
        professionistaGiorniDietaPazienteViewModel.setGiornoInModifica(position)

        val f: Fragment = ProfessionistaGiornoDietaFragment()

        activity?.supportFragmentManager?.beginTransaction()?.apply{
            replace(R.id.pazienti_fragment_container, f).addToBackStack("dietafrag-professionista").commit()
        }
    }

    override fun onEliminaDietaGiorno(item: GiornoItem, position: Int) {
        TODO("Not yet implemented")
    }
}