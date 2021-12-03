package it.polito.s279941.libra.professionistadieta

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.DataModel.GiornoDieta
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utentedieta.PastoItem
import kotlinx.android.synthetic.main.professionista_paziente_dieta_fragment.*

class GiornoAdapter (var btnClickListener: OnGiornoDietaRowButtonClickListener): RecyclerView.Adapter<GiornoAdapter.ViewHolder>() {
    private var giorniDieta:MutableList<GiornoDieta> = mutableListOf()
    fun setGiorniDieta( _giorniDieta:MutableList<GiornoDieta>){
        giorniDieta=_giorniDieta
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val numeroGiorno: TextView = v.findViewById(R.id.numeroGiorno)
        private val btnModificaGionroDieta: TextView = v.findViewById(R.id.idBtnModificaGionroDieta)
        private val btnEliminaGionroDieta: TextView = v.findViewById(R.id.idBtnEliminaGionroDieta)
        //private val tv_descrizione_pasto: TextView = v.findViewById(R.id.tv_descrizione_pasto)
        //private val cb_pasto_rispettato: CheckBox = v.findViewById(R.id.cb_pasto_rispettato)

        fun bind(item: GiornoDieta, position: Int, vhBtnClickListener: OnGiornoDietaRowButtonClickListener) {
            Log.d("aaaa1",position.toString())
            numeroGiorno.text = "Giorno: " + (position+1).toString()

            btnModificaGionroDieta.setOnClickListener { view ->
                Log.d("aaaa","btnModificaGionroDieta")
                vhBtnClickListener.onEditaDietaGiorno(item,position);
            }
            btnEliminaGionroDieta.setOnClickListener { view ->
                Log.d("aaaa","btnEliminaGionroDieta")
                vhBtnClickListener.onEliminaDietaGiorno(item,position);
            }


            //tv_descrizione_pasto.text = item.descrizione
            //cb_pasto_rispettato.isChecked=item.ho_rispettato
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
                R.layout.professionista_giornodieta_item,
                parent,
                false
            )
        );
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(giorniDieta[position],position,btnClickListener)
    }

    override fun getItemCount() : Int  {
        Log.d("aaaa-giorniDieta.size;", giorniDieta.size.toString())
        return giorniDieta.size;

    }

}
interface OnGiornoDietaRowButtonClickListener {
    fun onEditaDietaGiorno(item: GiornoDieta, position: Int)
    fun onEliminaDietaGiorno(item: GiornoDieta, position: Int)
}

