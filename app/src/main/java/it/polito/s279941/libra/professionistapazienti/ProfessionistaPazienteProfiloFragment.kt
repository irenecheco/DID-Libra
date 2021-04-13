package it.polito.s279941.libra.professionistapazienti

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utente.UtenteViewModel
import it.polito.s279941.libra.utenteobiettivi.ObiettiviAdapter
import kotlinx.android.synthetic.main.professionista_paziente_profilo_fragment.*

class ProfessionistaPazienteProfiloFragment: Fragment(R.layout.professionista_paziente_profilo_fragment) {

    val patientGoalsViewModel by activityViewModels<UtenteViewModel>()
    val patientGoalsAdapter = ObiettiviAdapter(); // stesso adapter usato per il lato utente (per ora (?))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Grafico
        val patientGraph = getView()?.findViewById(R.id.paziente_grafico) as GraphView
        // per visualizzare valori, poi sarà da implementare con i valori presi dal server per fare in modo che ogni volta si aggiorni da solo:
        val patientSeries = LineGraphSeries(arrayOf(DataPoint(0.12, 71.12), DataPoint(1.05, 75.43), DataPoint(2.22, 73.565),
                DataPoint(3.22, 78.65), DataPoint(4.7, 72.5), DataPoint(5.5, 73.8),
                DataPoint(6.7, 72.5), DataPoint(7.5, 73.8)));
        patientGraph.addSeries(patientSeries)
        patientSeries.color = Color.parseColor("#2EC4B6")
        patientGraph.title = "Peso (kg)"
        patientGraph.gridLabelRenderer.isHorizontalLabelsVisible = false
        patientGraph.gridLabelRenderer.isVerticalLabelsVisible = false
        patientGraph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE

        // Obiettivi
        patientGoalsViewModel.obiettiviStoricoLiveData.observe(viewLifecycleOwner, Observer { data -> patientGoalsAdapter.setObiettivi(data) })
        recyclerView_obiettivi_paziente.layoutManager= LinearLayoutManager(requireContext())
        recyclerView_obiettivi_paziente.adapter = patientGoalsAdapter
    }
}