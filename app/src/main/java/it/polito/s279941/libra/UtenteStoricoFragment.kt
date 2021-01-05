package it.polito.s279941.libra

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.utente_dieta_fragment.*

class UtenteStoricoFragment: Fragment(R.layout.utente_storico_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}