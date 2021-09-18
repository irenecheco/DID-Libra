package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.reactivex.disposables.Disposable
import it.polito.s279941.libra.R
import it.polito.s279941.libra.api.Api
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_login_page.*
import kotlinx.android.synthetic.main.fragment_login_page.view.*
import retrofit2.*

class LoginPageFragment : Fragment() {
    // istanzio il viewModel
    private val viewModel: LandingPageViewModel by activityViewModels()

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
        val loginButton : Button = view.findViewById(R.id.loginButton)
        Log.d(LOG_TAG, "LoginPageFragment called by pressing button with id : " + viewModel.selectedButtonId() + " in LandingPageFragment")
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in LoginPageFragment")

        // The singleton Api object is created lazily when the first time it is used.
        // After that it will be reused without creation
        val apiServer by lazy {
            Api.create()
        }
        // The disposable object is basically a return object from the RxJava 2.0 that tracks
        // the fetching activity
        var disposable: Disposable? = null

        loginButton.setOnClickListener {
            Log.d(LOG_TAG, "CLICK event on LOGIN button id: " + loginButton.id.toString() + " in LoginPageFragment")
            val disposable =  apiServer.getUsers()
            Log.d(LOG_TAG, "disposable: $disposable" )
        }
    }
}

