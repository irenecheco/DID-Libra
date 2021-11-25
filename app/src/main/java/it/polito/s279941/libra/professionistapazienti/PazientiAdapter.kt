package it.polito.s279941.libra.professionistapazienti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.DataModel.Obiettivo
import it.polito.s279941.libra.DataModel.Paziente
import it.polito.s279941.libra.DataModel.PazientiItem
import it.polito.s279941.libra.R
import it.polito.s279941.libra.landing.LandingPageViewModel
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
        private val lista_pazienti_nome: TextView = v.findViewById(R.id.nomePaziente)
        private val lista_pazienti_cognome: TextView = v.findViewById(R.id.cognomePaziente)
        private val paziente_button: Button = v.findViewById(R.id.paziente_button)

        fun bind(item: PazientiItem, action: OnPatientItemClickListener) {
            lista_pazienti_nome.text = item.nome_paziente
            lista_pazienti_cognome.text = item.cognome_paziente

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

    fun sortAlphabeticallyWithName(){
        val newPazienti = pazienti.sortedBy { it.nome_paziente }
        if (!newPazienti.isEmpty()){
            pazienti = newPazienti as MutableList<PazientiItem>
        }
        notifyDataSetChanged()
    }

    fun sortAlphabeticallyWithSurname(){
        val newPazienti = pazienti.sortedBy { it.cognome_paziente }
        if (!newPazienti.isEmpty()){
            pazienti = newPazienti as MutableList<PazientiItem>
        }
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
                        if (row.nome_paziente.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)) ||
                            row.cognome_paziente.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
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