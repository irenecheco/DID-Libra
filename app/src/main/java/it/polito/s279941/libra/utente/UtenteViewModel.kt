package it.polito.s279941.libra.utente

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.utentedieta.PastoItem
import it.polito.s279941.libra.utenteobiettivi.ObiettiviItem

class UtenteViewModel: ViewModel() {

    private var _giorno: Long = System.currentTimeMillis()
    private var _pastiDelGiorno = listOf(
        PastoItem("COLAZIONE", "una mela", true),
        PastoItem("SPUNTINO", "uno yogurt\nmela", false),
        PastoItem("PRANZO", "100gr farro\nun gelato \n6 ferrero rocher \n4 fragole \nun caffè con panna", true),
        PastoItem("MERENDA", "una noce \n1coca cola", true),
        PastoItem("CENA", "insalata \n300gr pasta carbonara \nscaloppine e patate al forno con maionese \n2 fette pandoro con nutella \n 1 tisana snellente", false)
    )
    private val _obiettiviStorico = mutableListOf(
        ObiettiviItem("Pesare 70kg", System.currentTimeMillis()-100000000),
        ObiettiviItem("Rispettare la dieta per tre giorni consecutivi", System.currentTimeMillis()-1000000000),
        ObiettiviItem("Pesarsi una volta a settimana per un mese", System.currentTimeMillis()-3000000000),
        ObiettiviItem("Diminuire i carboidrati", System.currentTimeMillis()-7000000000)
    )

    private val _pastiDelGiornoLiveData =
            MutableLiveData<List<PastoItem>>().also{it.value = _pastiDelGiorno}

    val pastiDelGiornoLiveData : LiveData<List<PastoItem>> =_pastiDelGiornoLiveData

    private val _obiettiviStoricoLiveData = MutableLiveData<List<ObiettiviItem>>().also{
        it.value = _obiettiviStorico
    }

    val obiettiviStoricoLiveData : LiveData<List<ObiettiviItem>> = _obiettiviStoricoLiveData

    fun setPastiDelGiorno(giorno: Long,pastiDelGiorno:List<PastoItem> ){
        _giorno= giorno
        _pastiDelGiorno=pastiDelGiorno
        _pastiDelGiornoLiveData.value=_pastiDelGiorno
    }
    fun getGiorno():Long{
        return _giorno
    }

    fun addObiettivi(obRaggiunto: String, obRaggiunto_data: Long) {
        val nuovoObiettivo = ObiettiviItem(obRaggiunto, obRaggiunto_data)
        _obiettiviStorico.add(nuovoObiettivo)
        _obiettiviStoricoLiveData.value = _obiettiviStorico
    }
}