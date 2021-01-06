package it.polito.s279941.libra.utentedieta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R

class PastoAdapter (var data: List<PastoItem>) : RecyclerView.Adapter<PastoAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tv_titolo_pasto: TextView = v.findViewById(R.id.tv_titolo_pasto);

        fun bind(item: PastoItem) {
         /*   data.text = DateFormat
                .getDateInstance(DateFormat.SHORT)
                .format(Date(item.data))
            prezzo.text = String.format("%5.2f", item.prezzo)
            stazione.text = item.stazione */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
           LayoutInflater.from(parent.context).inflate(
                R.layout.utente_pasto_item,
                parent,
                false
            )
        );
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size;

}