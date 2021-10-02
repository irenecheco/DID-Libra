package it.polito.s279941.libra.utente

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.utentedieta.PastoItem

class UtenteViewModel: ViewModel() {

    private var _noteDelGiorno: String = "note iniziali"
    private val _noteDelGiornoLiveData = MutableLiveData<String>().also{it.value = _noteDelGiorno}
    val noteDelGiornoLiveData : LiveData<String> = _noteDelGiornoLiveData
    fun setNoteDelGiorno(note:String ){
        _noteDelGiorno = note
        _noteDelGiornoLiveData.value = _noteDelGiorno
        // Salvarle anche sul database
    }

    private var _giorno: Long = System.currentTimeMillis()
    private val _giornoLiveData = MutableLiveData<Long>().also{it.value = _giorno}
    val giornoLiveData : LiveData<Long> = _giornoLiveData

    private var _pastiDelGiorno = listOf(
        PastoItem("COLAZIONE", "una mela!", true),
        PastoItem("SPUNTINO", "uno yogurt\nmela", false),
        PastoItem("PRANZO", "100gr farro\nun gelato \n6 ferrero rocher \n4 fragole \nun caffè con panna", true),
        PastoItem("MERENDA", "una noce \n1coca cola", true),
        PastoItem("CENA", "insalata \n300gr pasta carbonara \nscaloppine e patate al forno con maionese \n2 fette pandoro con nutella \n 1 tisana snellente", false)
    )

    private val _pastiDelGiornoLiveData =
            MutableLiveData<List<PastoItem>>().also{it.value = _pastiDelGiorno}

    val pastiDelGiornoLiveData : LiveData<List<PastoItem>> =_pastiDelGiornoLiveData

    fun setPastiDelGiorno(pastiDelGiorno:List<PastoItem> ){
        //_giorno= giorno
        //_giornoLiveData.value=_giorno
        _pastiDelGiorno=pastiDelGiorno
        _pastiDelGiornoLiveData.value=_pastiDelGiorno
    }
    fun getGiorno():Long{
        return _giorno
    }
    fun setGiorno(giorno:Long) {
        _giorno = giorno
        // TODO: capire se mettere giorno live data oppure basta pastiDelGiornoLiveData
        _giornoLiveData.value=_giorno

        // Carichiamo i pasti del giorno
        var nuoviPasti = listOf(
            PastoItem("COLAZIONE", "una pera!", true),
            PastoItem("SPUNTINO", "uno yogurt\nciliege", false),
            PastoItem("PRANZO", "100gr latte\nun gelato \n6 ferrero rocher \n4 fragole \nun caffè con panna", true),
            PastoItem("MERENDA", "una madoirloa \n1coca cola", true),
            PastoItem("CENA", "frittata \n300gr pasta carbonara \nscaloppine e patate al forno con maionese \n2 fette pandoro con nutella \n 1 tisana snellente", false)
        )
        setPastiDelGiorno(nuoviPasti)
    }

}