package it.polito.s279941.libra.utils

data class MainConfig(
    // The NodeJS server IP (solitamente il PC su cui gira NodeJS e AndroidStudio)
    // porta e path definiti in nodeJS. ATTENZIONE allo "/" finale
    val nodeJsBaseUrl : String = "https://192.168.63.240:3000/api/",
    // URL della ESP8266
    val esp8266URL : String = "http://192.168.4.1/libra/"
)
