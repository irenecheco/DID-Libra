package it.polito.s279941.libra.professionistadieta

import it.polito.s279941.libra.utentedieta.PastoItem

class GiornoItem(

    )
{

    // Qui i pasti del giorno li rappresentiamo in un elenco per comodità di utilizzo nella recicleview
    // ma poi sul database sono in una struttura fissa:
    // {
    //        nroGiorno: Number,
    //        colazione: String,
    //        spuntinoMattina: String,
    //        pranzo: String,
    //        spuntinoPomeriggio: String,
    //        cena: String
    //      }
    // la struttura da salvare sul DB la costruiamo al momento del
    // salvataggio e, al contrario, quando rileggiamo ritrasformiamo nell' elenco di sotto
    // Ricicliamo la classe PastoItem anche se ha in più l'attributo boolean che in questo caso non serve
    var _pastiDelGiorno = listOf(
        PastoItem("COLAZIONE", "una mela!", false),
        PastoItem("SPUNTINO", "uno yogurt\nmela", false),
        PastoItem("PRANZO", "100gr farro\nun gelato \n6 ferrero rocher \n4 fragole \nun caffè con panna", true),
        PastoItem("MERENDA", "una noce \n1coca cola", true),
        PastoItem("CENA", "insalata \n300gr pasta carbonara \nscaloppine e patate al forno con maionese \n2 fette pandoro con nutella \n 1 tisana snellente", false)
    )
}