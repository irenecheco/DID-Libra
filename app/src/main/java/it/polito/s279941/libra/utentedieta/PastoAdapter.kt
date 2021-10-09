package it.polito.s279941.libra.utentedieta

import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R
import it.polito.s279941.libra.professionistadieta.GiornoItem
import it.polito.s279941.libra.professionistadieta.OnGiornoDietaRowButtonClickListener


interface OnRowEditedChangeListener {
    fun onPastoConsumatoListener(item: PastoItem, position: Int, isChecked: Boolean)
}
class PastoAdapter (var onRowEditedChangeListener: OnRowEditedChangeListener) : RecyclerView.Adapter<PastoAdapter.ViewHolder>() {
    private var pastiDelGiorno: List<PastoItem> = emptyList()
    fun setPastiDelGiorno(_pastiDelGiorno:List<PastoItem>){
        pastiDelGiorno=_pastiDelGiorno
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tv_titolo_pasto: TextView = v.findViewById(R.id.tv_titolo_pasto)
        private val tv_descrizione_pasto: TextView = v.findViewById(R.id.tv_descrizione_pasto)
        private val cb_pasto_rispettato: CheckBox = v.findViewById(R.id.cb_pasto_rispettato)

        fun bind(item: PastoItem, position: Int, onRowEditedChangeListener: OnRowEditedChangeListener) {
            tv_titolo_pasto.text = item.titolo
            tv_descrizione_pasto.text = item.descrizione
            cb_pasto_rispettato.isChecked=item.ho_rispettato
            cb_pasto_rispettato.setOnCheckedChangeListener{ buttonView, isChecked ->
                onRowEditedChangeListener.onPastoConsumatoListener(item,position,isChecked)
            }
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
        holder.bind(pastiDelGiorno[position],position, onRowEditedChangeListener)
    }

    override fun getItemCount() = pastiDelGiorno.size;

}