package it.polito.s279941.libra.utils

const val LOG_TAG ="LIBRA"
const val LOG_TAG_ESP ="ESP8266"

/* https://developer.android.com/training/articles/security-config
           *** ATTENZIONE ! ***
Questi gli stessi IP vanno configurati nel file
       xml/network_security_config.xml           */

// The NodeJS server IP (solitamente il PC su cui gira NodeJS e AndroidStudio)
// porta e path definiti in nodeJS. ATTENZIONE allo "/" finale
// non è localhost!
const val BACKEND_URL = "http://192.168.13.240:3000/api/"
//const val BACKEND_URL = "https://192.168.254.240:8443/api/"

// URL di ESP8266
const val ESP8266_URL = "http://192.168.4.1/libra/"