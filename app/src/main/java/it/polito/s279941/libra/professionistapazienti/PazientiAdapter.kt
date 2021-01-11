package it.polito.s279941.libra.professionistapazienti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.s279941.libra.R
import java.text.DateFormat
import java.util.*

class PazientiAdapter () : RecyclerView.Adapter<PazientiAdapter.PazientiViewHolder>() {

    private var pazienti: List<PazientiItem> = emptyList()

    fun setPazienti(_pazienti: List<PazientiItem>){
        pazienti = _pazienti
        notifyDataSetChanged()
    }

    class PazientiViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val lista_pazienti_immagine: ImageView = v.findViewById(R.id.lista_pazienti_immagine)
        private val lista_pazienti_nome: TextView = v.findViewById(R.id.lista_pazienti_nome)
        private val lista_pazienti_data_ultimo_controllo: TextView = v.findViewById(R.id.lista_pazienti_data_ultimo_controllo)

        fun bind(item: PazientiItem) {
            //lista_pazienti_immagine.setImageDrawable(R.drawable.ic_profile)
            lista_pazienti_nome.text = item.nome_utente
            lista_pazienti_data_ultimo_controllo.text = DateFormat.getDateInstance(DateFormat.SHORT).format(Date(item.data_ultimo_controllo))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PazientiViewHolder {
        return PazientiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.professionista_paziente_item, parent, false))
    }

    override fun onBindViewHolder(holder: PazientiViewHolder, position: Int) {
        holder.bind(pazienti[position])
    }

    override fun getItemCount() = pazienti.size;

}