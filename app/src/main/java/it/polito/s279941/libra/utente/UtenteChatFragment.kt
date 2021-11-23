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

        mailDestinatario.text = utenteViewModel.utenteCorrente.cod_nutrizionista  // TODO non codice, ma mail nutrizionista

        //button click to get input and call sendEmail method
        button_email.setOnClickListener {
            //get input from EditTexts and save in variables
            val nut_mail = mailDestinatario.text.toString().trim()
            val subject = subject_input.text.toString().trim()
            val message = message_input.text.toString().trim()

            //method call for email intent with these inputs as parameters
            sendEmail(nut_mail, subject, message)
        }
    }

    private fun sendEmail(nut_mail: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(nut_mail))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}