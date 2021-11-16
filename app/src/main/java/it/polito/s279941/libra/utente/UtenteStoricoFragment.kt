package it.polito.s279941.libra.utente

import android.os.Bundle
import android.view.View
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


class UtenteStoricoFragment: Fragment(R.layout.utente_storico_fragment) {
    
    val goalsAdapter = ObiettiviAdapter()
    private val viewModel: UtenteViewModel by activityViewModels()

    private var lineChart: LineChart? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Grafico
        lineChart = getView()?.findViewById(R.id.utente_grafico)
        setLineChartData()

        // Obiettivi
        viewModel.obiettiviStorico.observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
        recyclerView_obiettivi.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_obiettivi.adapter = goalsAdapter
    }

    fun setLineChartData(){

        val weights = viewModel.pesoGrafico.value

        val xvalue = ArrayList<String>()
        xvalue.add("01/09")
        xvalue.add("02/10")
        xvalue.add("03/11")
        xvalue.add("04/12")
        xvalue.add("05/01")
        xvalue.add("06/02")
        xvalue.add("07/03")
        xvalue.add("08/04")
        // TODO date da inserire come per i valori dei pesi

        val lineEntry = ArrayList<Entry>()
        lineEntry.add(Entry(20f, 0))
        lineEntry.add(Entry(40f, 1))
        lineEntry.add(Entry(80f, 2))
        lineEntry.add(Entry(50f, 3))
        lineEntry.add(Entry(35f, 4))
        lineEntry.add(Entry(70f, 5))
        lineEntry.add(Entry(20f, 6))
        lineEntry.add(Entry(90f, 7))
        // TODO per pesi da server codice sotto commentato, ma non posso provare perchè q@q.com non ha pesi
        /*for((i, w) in weights!!.takeLast(8).withIndex()){
            w.peso?.let { Entry(it.toFloat(), i) }?.let {lineEntry.add(it)}
        }*/

        val lineDataSet = LineDataSet(lineEntry, "")
        lineDataSet.color = resources.getColor(R.color.colorPrimary)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius =5f
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = resources.getColor(R.color.colorPrimary)
        lineDataSet.fillAlpha = 30
        lineDataSet.valueTextColor = resources.getColor(R.color.colorPrimary)
        lineDataSet.valueTextSize = 15f

        lineChart?.setDescription("")
        lineChart?.xAxis?.position = XAxisPosition.BOTTOM
        lineChart?.xAxis?.textSize = 8f
        lineChart?.axisLeft?.setDrawLabels(false)
        lineChart?.axisRight?.setDrawLabels(false)
        lineChart?.legend?.isEnabled = false

        val data = LineData(xvalue, lineDataSet)

        lineChart?.data = data
        lineChart?.setBackgroundColor(resources.getColor(R.color.colorLight))
        lineChart?.animateXY(1000,1000)

    }

}