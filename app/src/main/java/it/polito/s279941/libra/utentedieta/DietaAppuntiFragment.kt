package it.polito.s279941.libra.utentedieta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import it.polito.s279941.libra.professionistadieta.GiornoItem
import it.polito.s279941.libra.professionistadieta.ProfessionistaGiornoDietaFragment
import it.polito.s279941.libra.utente.UtenteDietaFragment
import it.polito.s279941.libra.utente.UtenteViewModel
import kotlinx.android.synthetic.main.utente_dieta_appunti_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_calendario_fragment.view.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.*
import kotlinx.android.synthetic.main.utente_dieta_fragment.tv_today_data
import java.text.DateFormat

class DietaAppuntiFragment: Fragment(R.layout.utente_dieta_appunti_fragment) {

    val utenteViewModel by activityViewModels<UtenteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       utenteViewModel.noteDelGiornoLiveData.observe(viewLifecycleOwner,
       Observer { note -> tiCommento.setText(note) })

        salvaCommentoButton.setOnClickListener{
            utenteViewModel.saveNoteGiornoToDB(tiCommento.text.toString())

            this.activity?.onBackPressed()
            // val f: Fragment = UtenteDietaFragment()

               // activity?.supportFragmentManager?.beginTransaction()?.apply{
                 //   replace(R.id.utente_activity_main, f).addToBackStack("dietafrag-professionista").commit()
                // }

        }


        //tiCommento.addTextChangedListener(object : TextWatcher {
        //    override fun afterTextChanged(s: Editable?) {
        //    }

        //    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //   }

        //    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
         //       utenteViewModel._noteDelGiorno=tiCommento.text.toString()
          //  }
       // })

    }


}