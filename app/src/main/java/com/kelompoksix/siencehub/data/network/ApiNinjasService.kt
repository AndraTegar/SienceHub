package com.kelompoksix.siencehub.data.network

import com.kelompoksix.siencehub.data.models.FactItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNinjasService {
    @GET("v1/facts")
    suspend fun getFacts(
        @Header("X-Api-Key") apiKey: String,
        @Query("limit") limit: Int = 1
    ): List<FactItem>
}
