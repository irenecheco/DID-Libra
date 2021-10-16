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
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionistadieta.*
import it.polito.s279941.libra.utentedieta.DietaAppuntiFragment
import it.polito.s279941.libra.utentedieta.DietaCalendarioFragment
import kotlinx.android.synthetic.main.professionista_paziente_dieta_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import java.text.DateFormat
import java.util.*

//import java.util.*

class ProfessionistaPazienteDietaFragment: Fragment(R.layout.professionista_paziente_dieta_fragment),
    OnGiornoDietaRowButtonClickListener {
    val professionistaGiorniDietaPazienteViewModel by activityViewModels<ProfessionistaGiorniDietaPazienteViewModel>()
    val giornoAdapter = GiornoAdapter(this);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("aaaa","onViewCreated")
        // giornoAdapter.setGiorniDieta(professionistaGiorniDietaPazienteViewModel.getGiorniDietaPaziente())
        professionistaGiorniDietaPazienteViewModel.giorniLiveData.observe(viewLifecycleOwner, Observer {
                data -> giornoAdapter.setGiorniDieta(data)
        })
        recyclerViewGiorni.layoutManager= LinearLayoutManager(requireContext())
        recyclerViewGiorni.adapter = giornoAdapter


        addGiornoDietaPaziente.setOnClickListener { view ->
            Log.d("aaaaPiuBtn","onClick")
            professionistaGiorniDietaPazienteViewModel.addGiorno()
        }

        professionistaGiorniDietaPazienteViewModel.giornoInizioDietaLiveData.observe(viewLifecycleOwner,
            Observer { giorno -> tv_data_inizio.text= DateFormat.getDateInstance(DateFormat.LONG).format(
                Date(giorno)
            )
            })


        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.professionista_dieta_menu,menu)
        super.onCreateOptionsMenu(menu,menuInflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.menuDataInizio -> {
                //findNavController().navigate(R.id.action_utenteDietaFragment2_to_dietaAppuntiFragment)
                val f: Fragment = ProfessionistaCalendarioDietaDataInizio()
                activity?.supportFragmentManager?.beginTransaction()?.apply{
                    replace(R.id.pazienti_fragment_container, f).addToBackStack("dietafrag_caldatainizio").commit()
                }
            }
            //R.id.menuCalendario -> {
                //findNavController().navigate(R.id.action_utenteDietaFragment2_to_dietaCalendarioFragment)
              //  val f: Fragment = DietaCalendarioFragment()
               // activity?.supportFragmentManager?.beginTransaction()?.apply{
                 //   replace(R.id.fragment_container, f).addToBackStack("dietafrag_calendar").commit()
                    //add(R.id.fragment_container, f).commit()
              //  }
          //  }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onEditaDietaGiorno(item: GiornoDieta, position: Int) {
        professionistaGiorniDietaPazienteViewModel.setGiornoInModifica(position)

        val f: Fragment = ProfessionistaGiornoDietaFragment()

        activity?.supportFragmentManager?.beginTransaction()?.apply{
            replace(R.id.pazienti_fragment_container, f).addToBackStack("dietafrag-professionista").commit()
        }
    }

    override fun onEliminaDietaGiorno(item: GiornoDieta, position: Int) {
        TODO("Not yet implemented")
    }
}