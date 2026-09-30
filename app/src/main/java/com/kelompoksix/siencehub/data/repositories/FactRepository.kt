package com.kelompoksix.siencehub.data.repositories

import com.kelompoksix.siencehub.data.network.ApiNinjasService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FactRepository {
    private const val BASE_URL = "https://api.api-ninjas.com/"

    // API Key Ninjas
    var apiKey: String = "1hNnkHuMGSYREkWK8jVcqEbOtxbmj7wwFJXFGeOb"

    private val apiService: ApiNinjasService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiNinjasService::class.java)
    }

    private val fallbackFacts = listOf(
        "Awan kumulus rata-rata memiliki berat sekitar 500.000 kg (setara 100 ekor gajah).",
        "Cahaya matahari membutuhkan waktu 8 menit dan 20 detik untuk sampai ke permukaan Bumi.",
        "Inti dalam Bumi memiliki suhu sekitar 5.400°C, mendekati suhu permukaan Matahari.",
        "Satu sendok teh bintang netron memiliki massa sekitar 6 miliar ton.",
        "DNA manusia 99,9% identik antar sesama manusia, dan sekitar 60% mirip dengan pisang!",
        "Petir menyambar Bumi sekitar 100 kali setiap detiknya di seluruh dunia.",
        "Air hangat bisa membeku lebih cepat daripada air dingin dalam kondisi tertentu (Efek Mpemba).",
        "Neptunus membutuhkan waktu sekitar 165 tahun di Bumi untuk satu kali mengelilingi Matahari."
    )

    suspend fun getScienceFact(): String {
        if (apiKey.isBlank() || apiKey == "YOUR_API_NINJAS_KEY") {
            return fallbackFacts.random()
        }

        return try {
            val response = apiService.getFacts(apiKey = apiKey, limit = 1)
            if (response.isNotEmpty() && response[0].fact.isNotBlank()) {
                response[0].fact
            } else {
                fallbackFacts.random()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fallbackFacts.random()
        }
    }
}
