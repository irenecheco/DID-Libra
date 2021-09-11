package it.polito.s279941.libra

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.s279941.libra.utenteobiettivi.ObiettiviItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: Repository) : ViewModel() {
    val goalList = MutableLiveData<List<ObiettiviItem>>()
    val errorMessage = MutableLiveData<String>()

    fun getGoals() {

        val response = repository.getGoals()
        response.enqueue(object : Callback<List<ObiettiviItem>> {
            override fun onResponse(call: Call<List<ObiettiviItem>>, response: Response<List<ObiettiviItem>>) {
                goalList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<ObiettiviItem>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}