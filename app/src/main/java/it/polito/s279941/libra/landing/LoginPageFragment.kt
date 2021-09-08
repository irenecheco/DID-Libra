package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposable
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api
import kotlinx.android.synthetic.main.fragment_login_page.*
import kotlinx.android.synthetic.main.fragment_login_page.view.*
import retrofit2.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginPageFragment : Fragment() {
    val myName : String = ""
    val myPassword : String = ""
    // The NodeJS server IP (solitamente il PC su cui gira NodeJS e AndroidStudio)
    val url : String = "https://192.168.63.240"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginB : Button = view.findViewById(R.id.loginButton)

        // The singleton Api object is created lazily when the first time it is used.
        // After that it will be reused without creation
        val apiServer by lazy {
            Api.create()
        }
        // The disposable object is basically a return object from the RxJava 2.0 that tracks
        // the fetching activity
        var disposable: Disposable? = null

        loginB.setOnClickListener {
            Log.d("LIBRA", "CLICK event on LOGIN button")
            val disposable =  apiServer.getUsers()
            Log.d("LIBRA", "disposable: $disposable" )
        }
    }
}

