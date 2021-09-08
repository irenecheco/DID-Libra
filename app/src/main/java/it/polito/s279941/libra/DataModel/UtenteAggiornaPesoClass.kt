package it.polito.s279941.libra.DataModel

//Classe per salvare il peso ottenuto da bilancia:
//bilancia mi manda JSON, così lo converto in dato utilizzabile in Kotlin
//Double in questo caso
data class UtenteAggiornaPesoClass (
        val get_weight: Double
)
