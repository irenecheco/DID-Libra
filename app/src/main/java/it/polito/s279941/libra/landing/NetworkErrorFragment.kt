package it.polito.s279941.libra.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.polito.s279941.libra.R
import kotlinx.android.synthetic.main.fragment_network_error.*

/**
 * A simple [Fragment] subclass.
 * Use the [NetworkErrorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NetworkErrorFragment : Fragment() {

    companion object {
        /* Use this factory method to create a new instance of this fragment */
        @JvmStatic
        fun newInstance() =
            NetworkErrorFragment().apply {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network_error, container, false)
    }

    // indica al frammento che è terminata la creazione dell'activity
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // esco dall'applicazione
        quitButton.setOnClickListener{
            //Log.d(LOG_TAG, "event CLICK on quitButton button id: " + quitButton.id.toString() + " in NetworkErrorFragment")
            (activity as LandingPageActivity?)?.quitApp()
        }
    }

}