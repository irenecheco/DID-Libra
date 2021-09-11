package it.polito.s279941.libra.DataModel

//Classe per salvare il peso ottenuto da bilancia:
//bilancia mi manda JSON, così lo converto in dato utilizzabile in Kotlin
//Double in questo caso
// json restituito: {"get_weight":0.00}
data class UtenteAggiornaPesoClass (
        val get_weight: Double
)

/* data class per inizializzare la bilancia */
// json restituito: {"init_scale":"ready_to_weight"}
data class UtenteAvviaBilanciaClass (
        val init_scale: String
)