package it.polito.s279941.libra.utente

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.utente_chat_fragment.*

class UtenteChatFragment: Fragment(R.layout.utente_chat_fragment) {

    private val utenteViewModel: UtenteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mailDestinatario.text = utenteViewModel.utenteCorrente.email_nutrizionista

        // per inviare
        button_email.setOnClickListener {
            // recupera mail, destinatario e messaggio dagli input
            val nut_mail = mailDestinatario.text.toString().trim()
            val subject = subject_input.text.toString().trim()
            val message = message_input.text.toString().trim()

            // richiamo al metodo per l'email intent
            sendEmail(nut_mail, subject, message)
        }
    }

    private fun sendEmail(nut_mail: String, subject: String, message: String) {
        // ACTION_SEND per lanciare un email client installato sul dispositivo
        val mIntent = Intent(Intent.ACTION_SEND)
        // per mandare una mail bisogna specificare mailto  -->  URI
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // mettere il destinatario nell'intent -->  uso array perchè i destinatari potrebbero essere più di uno
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(nut_mail))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)    // mettere l'oggetto nell'intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)    // mettere il messaggio nell'intent


        try {
            // start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            // per gestire gli eventuali problemi ed eccezioni
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}