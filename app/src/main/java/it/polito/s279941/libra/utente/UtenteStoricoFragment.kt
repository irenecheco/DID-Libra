package it.polito.s279941.libra.utente

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import it.polito.s279941.libra.*
import it.polito.s279941.libra.utenteobiettivi.ObiettiviAdapter
import kotlinx.android.synthetic.main.utente_storico_fragment.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import kotlinx.android.synthetic.main.utente_profilo_fragment.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class UtenteStoricoFragment: Fragment(R.layout.utente_storico_fragment) {
    
    val goalsAdapter = ObiettiviAdapter()
    private val utenteViewModel: UtenteViewModel by activityViewModels()

    private var lineChart: LineChart? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nome_utente.text = utenteViewModel.utenteCorrente.nome
        cognome_utente.text = utenteViewModel.utenteCorrente.cognome

        // Grafico
        lineChart = getView()?.findViewById(R.id.utente_grafico)
        setLineChartData()

        // Obiettivi
        noObiettivi.visibility = View.GONE
        if(utenteViewModel.utenteCorrente.obiettivi?.size != 0) {
            utenteViewModel.obiettiviStorico.observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
            recyclerView_obiettivi.layoutManager = LinearLayoutManager(requireContext())
            recyclerView_obiettivi.adapter = goalsAdapter
        } else {
            noObiettivi.visibility = View.VISIBLE
        }
    }

    fun setLineChartData(){

        val weights = utenteViewModel.pesoGrafico.value

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
        lineChart?.xAxis?.position = XAxisPosition.BOTTOM
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