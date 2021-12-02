package it.polito.s279941.libra.professionista

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.professionista_profilo_fragment.*
import kotlinx.android.synthetic.main.utente_profilo_fragment.*
import java.text.SimpleDateFormat

class ProfessionisaProfiloFragment: Fragment(R.layout.professionista_profilo_fragment) {
    private val professionistaViewModel: ProfessionistaViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nutr_user_name2.text = professionistaViewModel.utenteCorrente.nome
        nutr_user_name.text = professionistaViewModel.utenteCorrente.cognome
        nutr_id.text = professionistaViewModel.utenteCorrente.cod_nutrizionista
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateString = simpleDateFormat.format(professionistaViewModel.utenteCorrente.data_nascita)
        nutr_dateOfBirth.text = (dateString).toString()
        nutr_email.text = professionistaViewModel.utenteCorrente.email
        text_number.text = professionistaViewModel.utenteCorrente.lista_pazienti?.size.toString()


    }
}
