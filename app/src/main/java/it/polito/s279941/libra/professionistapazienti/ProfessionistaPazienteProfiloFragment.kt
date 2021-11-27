package it.polito.s279941.libra.professionistapazienti

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utente.UtenteViewModel
import it.polito.s279941.libra.utenteobiettivi.ObiettiviAdapter
import it.polito.s279941.libra.utils.LOG_TAG
import kotlinx.android.synthetic.main.professionista_paziente_profilo_fragment.*
import kotlinx.android.synthetic.main.utente_storico_fragment.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class ProfessionistaPazienteProfiloFragment: Fragment(R.layout.professionista_paziente_profilo_fragment) {

    val goalsAdapter = ObiettiviAdapter()
    private val pazienteViewModel: ProfessionistaPazienteViewModel by activityViewModels()

    private var lineChart: LineChart? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nome_paziente.text = pazienteViewModel.pazienteCorrente.nome
        cognome_paziente.text = pazienteViewModel.pazienteCorrente.cognome
        data_nascita_paziente.text = DateFormat.getDateInstance(DateFormat.SHORT).format(pazienteViewModel.pazienteCorrente.data_nascita)
        mail_paziente.text = pazienteViewModel.pazienteCorrente.email

        // Grafico
        lineChart = getView()?.findViewById(R.id.paziente_grafico)
        setLineChartData()

        // Obiettivi
        noObiettivi_paziente.visibility = View.GONE
        if(pazienteViewModel.pazienteCorrente.obiettivi?.size != 0) {
            pazienteViewModel.obiettiviStorico.observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
            recyclerView_obiettivi_paziente.layoutManager = LinearLayoutManager(requireContext())
            recyclerView_obiettivi_paziente.adapter = goalsAdapter
        } else {
            noObiettivi_paziente.visibility = View.VISIBLE
        }


        val newFragment: Fragment = ProfessionistaPazienteAggiungiObiettivoFragment()
        add_goal_button.setOnClickListener{
            Log.d(LOG_TAG, "event CLICK on ADD GOAL in ProfessionistaPazienteProfiloFragment")
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.pazienti_fragment_container, newFragment)
            transaction?.addToBackStack("ProfessionistaPazienteProfiloFragment")
            transaction?.commit()
        }
    }

    fun setLineChartData(){

        val weights = pazienteViewModel.pesoGrafico.value

        val xvalue = ArrayList<String>()
        var dataGraph = ""
        for((i, w) in weights!!.takeLast(8).withIndex()){
            w.data?.let {
                val simpleDateFormat = SimpleDateFormat("dd/MM")
                dataGraph = simpleDateFormat.format(it)
            }?.let {xvalue.add(dataGraph)}
        }

        val lineEntry = ArrayList<Entry>()
        for((i, w) in weights.takeLast(8).withIndex()){
            w.peso?.let { Entry(it.toFloat(), i) }?.let {lineEntry.add(it)}
        }

        val lineDataSet = LineDataSet(lineEntry, "")
        lineDataSet.color = ResourcesCompat.getColor(getResources(), R.color.colorGraph, null)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 5f
        lineDataSet.setCircleColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null))
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ResourcesCompat.getColor(getResources(), R.color.colorGraph, null)
        lineDataSet.fillAlpha = 30
        lineDataSet.valueTextColor = ResourcesCompat.getColor(getResources(), R.color.colorGraph, null)
        lineDataSet.valueTextSize = 15f

        lineChart?.setDescription("")
        lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        lineChart?.xAxis?.textSize = 9.5f
        lineChart?.xAxis?.textColor = ResourcesCompat.getColor(getResources(), R.color.colorSnackbar, null)
        lineChart?.axisLeft?.setDrawLabels(false)
        lineChart?.axisRight?.setDrawLabels(false)
        lineChart?.legend?.isEnabled = false
        lineChart?.setScaleEnabled(false)

        val data = LineData(xvalue, lineDataSet)

        lineChart?.data = data
        //lineChart?.animateXY(200,200)

    }
}