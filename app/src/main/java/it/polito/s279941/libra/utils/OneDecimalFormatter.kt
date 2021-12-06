package it.polito.s279941.libra.utils

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

// https://titanwolf.org/Network/Articles/Article?AID=feb0bbc9-41da-43b9-866b-55fe9b2d788a
// https://weeklycoding.com/mpandroidchart-documentation/formatting-data-values/

/* Questa classe definisce la formattazione per il grafico */
class OneDecimalFormatter: ValueFormatter {

    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        return String.format("%.1f", value)
    }
}