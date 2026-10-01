package com.kelompoksix.siencehub.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompoksix.siencehub.data.repositories.FactRepository
import kotlinx.coroutines.launch

class BerandaViewModel : ViewModel() {
    var faktaSains by mutableStateOf("Mengambil fakta sains dari API Ninjas...")
        private set

    var isLoadingFakta by mutableStateOf(false)
        private set

    init {
        muatFaktaBaru()
    }

    fun muatFaktaBaru() {
        if (isLoadingFakta) return
        viewModelScope.launch {
            isLoadingFakta = true
            faktaSains = FactRepository.getScienceFact()
            isLoadingFakta = false
        }
    }
}
