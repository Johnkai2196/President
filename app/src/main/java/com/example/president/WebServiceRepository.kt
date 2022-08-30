package com.example.president

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URL

object Api {
    val url = URL("https://en.wikipedia.org/w/")

    object Hits {
        data class Query(val query: String)
        data class SearchInfo(val searchinfo: Query)
        data class Totalhits(val totalhits: SearchInfo)
    }

    interface Service {
        @GET("api.php")
        suspend fun getHits(
            @Query("action") action: String,
            @Query("format") format: String,
            @Query("list") list: String,
            @Query("srsearch") name: String
        ): Hits.Totalhits
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!
}

class WikiRepository {
    private val call = Api.service
    suspend fun getUser(name: String) = call.getHits("query","json","search",name)
}
