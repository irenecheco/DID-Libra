package it.polito.s279941.libra.professionistadieta

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R
import it.polito.s279941.libra.utentedieta.PastoItem
import android.text.Editable

import android.text.TextWatcher
import it.polito.s279941.libra.DataModel.GiornoDieta


class PastoAdapter () : RecyclerView.Adapter<PastoAdapter.ViewHolder>() {
    private var pastiDelGiorno: MutableList<PastoItem> = mutableListOf<PastoItem>()
    fun setPastiDelGiorno(giornoDieta: GiornoDieta){
        Log.d("Nro pasti!!!!!!!:","-----------")
        //Log.d("Nro pasti:",_pastiDelGiorno.size.toString())

        // Faccio una copia dei dati in modo da non sovrascrivere i dati originali nel caso non salvasse le modifiche
        pastiDelGiorno= mutableListOf(
            PastoItem("COLAZIONE", giornoDieta.colazione?:"", false),
            PastoItem("SPUNTINO", giornoDieta.spuntinoMattina?:"", false),
            PastoItem("PRANZO", giornoDieta.pranzo?:"", false),
            PastoItem("MERENDA",giornoDieta.spuntinoPomeriggio?:"", true),
            PastoItem("CENA",giornoDieta.cena?:""
                , false)
        )

        notifyDataSetChanged();
    }
    fun getPastiDelGiorno(): List<PastoItem> {
        return pastiDelGiorno
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tv_titolo_pasto: TextView = v.findViewById(R.id.tv_titolo_pasto)
        private val tv_descrizione_pasto: TextView = v.findViewById(R.id.tv_descrizione_pasto)
        //private val cb_pasto_rispettato: CheckBox = v.findViewById(R.id.cb_pasto_rispettato)

        fun bind(item: PastoItem) {
            tv_titolo_pasto.text = item.titolo
            tv_descrizione_pasto.text = item.descrizione

            tv_descrizione_pasto.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    item.descrizione = s.toString()
                }
            })


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
                R.layout.professionista_pasto_item,
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