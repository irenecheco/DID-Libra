package it.polito.s279941.libra.utente

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import it.polito.s279941.libra.*
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.utenteobiettivi.ObiettiviAdapter
import it.polito.s279941.libra.utenteobiettivi.ObiettiviViewModel
import kotlinx.android.synthetic.main.utente_storico_fragment.*
import android.widget.Toast
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.Series
import com.jjoe64.graphview.series.OnDataPointTapListener
import com.jjoe64.graphview.DefaultLabelFormatter

class UtenteStoricoFragment: Fragment(R.layout.utente_storico_fragment) {
    
    val goalsAdapter = ObiettiviAdapter()
    private lateinit var goalsViewModel: ObiettiviViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalsViewModel = ViewModelProvider(this).get(ObiettiviViewModel::class.java)
        Log.d("LIBRAgoals","1.  calling & create the viewModel of class ObiettiviVieModel in UtenteStoricoFragment")

        // Grafico
        val graph = getView()?.findViewById(R.id.utente_grafico) as GraphView
        // per visualizzare valori, poi sarà da implementare con i valori presi dal server per fare in modo che ogni volta si aggiorni da solo:
        val series = LineGraphSeries(arrayOf(DataPoint(0.12, 71.12), DataPoint(1.05, 75.43), DataPoint(2.22, 73.565),
            DataPoint(3.22, 78.65), DataPoint(4.7, 72.5), DataPoint(5.5, 73.8),
            DataPoint(6.7, 72.5), DataPoint(7.5, 73.8)));
        graph.addSeries(series)
        series.color = Color.parseColor("#2EC4B6")
        graph.title = "Peso (kg)"
        graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        graph.gridLabelRenderer.isVerticalLabelsVisible = false
        graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE

        // tap listener on data
        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(activity,"$dataPoint", Toast.LENGTH_SHORT).show()
        }

        // labels and label formatter
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    // show normal x values
                    super.formatLabel(value, isValueX)
                } else {
                    // show currency for y values
                    super.formatLabel(value, isValueX) + " kg"
                }
            }
        }

        // Obiettivi
        //goalsViewModel.obiettiviStoricoLiveData.observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
        goalsViewModel.getGoalsRepository().observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
        recyclerView_obiettivi.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_obiettivi.adapter = goalsAdapter
        Log.d("LIBRAgoals","7.  return from call viewModel.getGoals().toString()  in UtenteStoricoFragment")
    }

}