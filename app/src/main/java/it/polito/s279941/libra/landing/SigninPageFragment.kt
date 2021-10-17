package it.polito.s279941.libra.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.fragment_signin_page.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SigninPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SigninPageFragment : Fragment() {
    // creo il rif al viewModel istanziato dall'activity
    private val viewModel: LandingPageViewModel by activityViewModels()


    companion object {
        /* Use this factory method to create a new instance of this fragment */
        @JvmStatic
        fun newInstance() = SigninPageFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(LOG_TAG, "SigninPageFragment called by pressing button with id : " + viewModel.getSelectedButtonId() + " in LandingPageFragment") //--->DBG
        Log.d(LOG_TAG, "viewModel: " + viewModel.toString() + " in SigninPageFragment") //--->DBG

        //setto datePicker a data corrente
        val oggi = Calendar.getInstance()
        datePickerBirthday.init(
            oggi.get(Calendar.YEAR),
            oggi.get(Calendar.MONTH),
            oggi.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
        }

    }


}