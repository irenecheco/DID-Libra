package it.polito.s279941.libra.professionistapazienti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R
import java.text.DateFormat
import java.util.*

class PazientiAdapter (var clickListener: OnPatientItemClickListener) : RecyclerView.Adapter<PazientiAdapter.PazientiViewHolder>(), Filterable {

    private var pazienti: MutableList<PazientiItem> = mutableListOf()
    var pazientiFilter: MutableList<PazientiItem> = mutableListOf()

    fun setPazienti(_pazienti: MutableList<PazientiItem>){
        pazienti = _pazienti
        pazientiFilter = _pazienti
        notifyDataSetChanged()
    }

    class PazientiViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val lista_pazienti_immagine: ImageView = v.findViewById(R.id.lista_pazienti_immagine)
        private val lista_pazienti_nome: TextView = v.findViewById(R.id.numeroGiorno)
        private val lista_pazienti_data_ultimo_controllo: TextView = v.findViewById(R.id.lista_pazienti_data_ultimo_controllo)
        private val paziente_button: Button = v.findViewById(R.id.paziente_button)

        fun bind(item: PazientiItem, action: OnPatientItemClickListener) {
            //lista_pazienti_immagine.setImageResource(R.drawable.ic_profile)
            lista_pazienti_nome.text = item.nome_utente
            lista_pazienti_data_ultimo_controllo.text = DateFormat.getDateInstance(DateFormat.SHORT).format(Date(item.data_ultimo_controllo))

            paziente_button.setOnClickListener{
                action.onItemClick(item, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PazientiViewHolder {
        return PazientiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.professionista_paziente_item, parent, false))
    }

    override fun onBindViewHolder(holder: PazientiViewHolder, position: Int) {
        holder.bind(pazienti[position],clickListener)
    }

    override fun getItemCount() = pazienti.size;

    fun sortAlphabetically(){
        val newPazienti = pazienti.sortedBy { it.nome_utente }
        pazienti = newPazienti as MutableList<PazientiItem>
        notifyDataSetChanged()
    }

    fun sortChronologically(){
        val newPazienti = pazienti.sortedBy { it.data_ultimo_controllo }
        pazienti = newPazienti as MutableList<PazientiItem>
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    pazienti = pazientiFilter
                } else {
                    val resultList = mutableListOf<PazientiItem>()
                    pazienti = pazientiFilter
                    for (row in pazienti) {
                        if (row.nome_utente.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    pazienti = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = pazienti
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pazienti = results?.values as MutableList<PazientiItem>
                notifyDataSetChanged()
            }

        }
    }
}

interface OnPatientItemClickListener{
    fun onItemClick(item: PazientiItem, position: Int)
}