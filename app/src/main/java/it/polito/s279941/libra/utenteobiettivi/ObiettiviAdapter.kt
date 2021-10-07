package it.polito.s279941.libra.utenteobiettivi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.R
import java.text.DateFormat
import java.util.*

class ObiettiviAdapter () : RecyclerView.Adapter<ObiettiviAdapter.ObiettiviViewHolder>() {

    private var obiettiviStorico: List<Obiettivo> = emptyList()

    fun setObiettivi(_obiettiviStorico: List<Obiettivo>){
        obiettiviStorico = _obiettiviStorico
        notifyDataSetChanged()
    }

    class ObiettiviViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val utente_obiettivo_raggiunto: TextView = v.findViewById(R.id.utente_obiettivo_raggiunto)
        private val utente_data_obiettivo: TextView = v.findViewById(R.id.utente_data_obiettivo)

        fun bind(item: Obiettivo) {
            utente_obiettivo_raggiunto.text = item.obiettivo
            utente_data_obiettivo.text = DateFormat.getDateInstance(DateFormat.SHORT).format(item.data_obiettivo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObiettiviViewHolder {
        return ObiettiviViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.utente_obiettivo_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ObiettiviViewHolder, position: Int) {
        holder.bind(obiettiviStorico[position])
    }

    override fun getItemCount() = obiettiviStorico.size;

}