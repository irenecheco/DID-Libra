package it.polito.s279941.libra.professionistapazienti

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionistadieta.*
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_paziente_dieta_fragment.*
import java.text.DateFormat
import java.util.*

class ProfessionistaPazienteDietaFragment: Fragment(R.layout.professionista_paziente_dieta_fragment),
    OnGiornoDietaRowButtonClickListener {
    val professionistaGiorniDietaPazienteViewModel by activityViewModels<ProfessionistaGiorniDietaPazienteViewModel>()
    val giornoAdapter = GiornoAdapter(this);

    @SuppressLint("StringFormatInvalid")
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

        val tv_data: TextView = view.findViewById(R.id.tv_data_inizio_dieta)
        tv_data.setOnClickListener { view ->
            val f: Fragment = ProfessionistaCalendarioDietaDataInizio()
            activity?.supportFragmentManager?.beginTransaction()?.apply{
                replace(R.id.pazienti_fragment_container, f).addToBackStack("dietafrag_caldatainizio").commit()
            }
        }

        professionistaGiorniDietaPazienteViewModel.giornoInizioDietaLiveData.observe(viewLifecycleOwner,
            Observer {
                    giorno ->
                Log.d(LOG_TAG,"  start Lambda expression  in ProfessionistaPazienteDietaFragment.onViewCreated() ")
                tv_data_inizio.text = DateFormat.getDateInstance(DateFormat.LONG).format(Date(giorno))
                tv_data_inizio_dieta.text = getString(R.string.tv_data_inizio_dieta_prefisso, tv_data_inizio.text)
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
        dialogYesOrNo(
            this.requireActivity(),
            getString(R.string.title_confirm_text),
            getString(R.string.message_confirm_text),
            DialogInterface.OnClickListener { dialog, id ->
                //utenteDietaRepository.saveDieta(paziente.value!!._id,paziente.value!!.dieta!!)
                professionistaGiorniDietaPazienteViewModel.eliminaGiornoDieta(position)
                professionistaGiorniDietaPazienteViewModel.saveDietaPaziente()
            }
        )
    }


    fun dialogYesOrNo(
        activity: Activity,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton(R.string.ok_text, DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            listener.onClick(dialog, id)
        })
        builder.setNegativeButton(R.string.no_text, null)
        val alert = builder.create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }


}