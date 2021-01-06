package it.polito.s279941.libra.utentedieta

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R

class PastoAdapter () : RecyclerView.Adapter<PastoAdapter.ViewHolder>() {
    private var pastiDelGiorno: List<PastoItem> = emptyList()
    fun setPastiDelGiorno(_pastiDelGiorno:List<PastoItem>){
        pastiDelGiorno=_pastiDelGiorno
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tv_titolo_pasto: TextView = v.findViewById(R.id.tv_titolo_pasto)
        private val tv_descrizione_pasto: TextView = v.findViewById(R.id.tv_descrizione_pasto)
        private val cb_pasto_rispettato: CheckBox = v.findViewById(R.id.cb_pasto_rispettato)

        fun bind(item: PastoItem) {
            tv_titolo_pasto.text = item.titolo
            tv_descrizione_pasto.text = item.descrizione
            cb_pasto_rispettato.isChecked=item.ho_rispettato
                // DateFormat
               // .getDateInstance(DateFormat.SHORT)
              //  .format(Date(item.data))
           // prezzo.text = String.format("%5.2f", item.prezzo)
            //stazione.text = item.stazione
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
        holder.bind(pastiDelGiorno[position])
    }

    override fun getItemCount() = pastiDelGiorno.size;

}