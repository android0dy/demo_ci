package com.ter.cidemo.data.api

import com.ter.cidemo.data.room.model.CurrencyInfo
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Declaration of API method
 */
interface ApiService {
    @GET()
    fun obtainData(@Url url: String): Single<List<CurrencyInfo>>
}
