package it.polito.s279941.libra.landing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionista.ProfessionistaMainActivity
import it.polito.s279941.libra.utente.UtenteMainActivity
import kotlinx.android.synthetic.main.fragment_landing_page.*


/**
 * A simple [Fragment] subclass.
 * Use the [LandingPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        //Qui può essere eseguita la personalizzazione della GUI
    }


    // indica al frammento che è terminata la creazione dell'activity
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val login_page_fragment = LoginPageFragment()

        utente_button.setOnClickListener{
            val i = Intent(getActivity(), UtenteMainActivity::class.java)
            startActivityForResult(i, 1)
        }

        professionista_button.setOnClickListener{
            val i = Intent(getActivity(), ProfessionistaMainActivity::class.java)
            startActivityForResult(i, 1)
        }

        preLoginButton.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.landing_page_fragment_container, login_page_fragment)
            transaction?.addToBackStack("LandigPageFragment")
            transaction?.commit()
        }
    }

}