package it.polito.s279941.libra.utente

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import it.polito.s279941.libra.MainViewModel
import it.polito.s279941.libra.R
import it.polito.s279941.libra.Repository
import it.polito.s279941.libra.ViewModelFactory
import it.polito.s279941.libra.api.Api
import it.polito.s279941.libra.utenteobiettivi.ObiettiviAdapter
import kotlinx.android.synthetic.main.utente_storico_fragment.*

class UtenteStoricoFragment: Fragment(R.layout.utente_storico_fragment) {

    //val utenteGoalsViewModel by activityViewModels<UtenteViewModel>()
    lateinit var utenteGoalsViewModel: MainViewModel
    val goalsAdapter = ObiettiviAdapter()

    private val retrofitService = Api.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // Obiettivi
        utenteGoalsViewModel = ViewModelProvider(this, ViewModelFactory(Repository(retrofitService))).get(MainViewModel::class.java)
        //utenteGoalsViewModel.obiettiviStoricoLiveData.observe(viewLifecycleOwner, Observer { data -> goalsAdapter.setObiettivi(data) })
        recyclerView_obiettivi.layoutManager= LinearLayoutManager(requireContext())
        recyclerView_obiettivi.adapter = goalsAdapter
        utenteGoalsViewModel.goalList.observe(viewLifecycleOwner, Observer{ goalsAdapter.setGoalList(it) })
        utenteGoalsViewModel.errorMessage.observe(viewLifecycleOwner, Observer{})
        utenteGoalsViewModel.getGoals()
    }
}