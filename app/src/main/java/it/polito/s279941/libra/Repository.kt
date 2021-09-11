package it.polito.s279941.libra

import it.polito.s279941.libra.api.Api

class Repository constructor(private val retrofitService: Api) {
    fun getGoals() = retrofitService.getGoals()
}