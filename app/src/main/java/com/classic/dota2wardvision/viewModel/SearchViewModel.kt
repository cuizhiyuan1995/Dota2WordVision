package com.classic.dota2wardvision.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classic.dota2wardvision.openDotAAPI.NetworkModule
import com.classic.dota2wardvision.openDotAAPI.OpenDotaRepository
import com.classic.dota2wardvision.openDotAAPI.PlayerSearchResponse
import com.classic.dota2wardvision.openDotAAPI.WardMapResponse
import com.classic.dota2wardvision.room.PlayerReport
import com.classic.dota2wardvision.room.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: OpenDotaRepository,
    private val repository: PlayerRepository
) : ViewModel() {
    var searchResults by mutableStateOf<List<PlayerSearchResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var networkerror by mutableStateOf(false)
        private set
    fun setNetworkError(value: Boolean) {
        networkerror = value
    }

    var playerID by mutableStateOf<Long?>(null)
        private set

    private val _analyticsData = MutableStateFlow<WardMapResponse?>(null)
    val analyticsData: StateFlow<WardMapResponse?> = _analyticsData

    private val _isReportLoading = MutableStateFlow(false)
    val isReportLoading: StateFlow<Boolean> = _isReportLoading


    fun searchPlayer(name: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            if(repo.searchPlayers(name) != null){
                searchResults = repo.searchPlayers(name)!!
            }
            else{
                networkerror = true
            }
            isLoading = false
            onComplete?.invoke()
        }
    }

    fun generateReport(playerAccountId: PlayerSearchResponse) {
        // Parse player data and store in LiveData / StateFlow
        viewModelScope.launch {
            _isReportLoading.value = true
            try {
                // 1️⃣ Get most recent 10 matches
                val wardmap = repo.getWardMap(playerAccountId.account_id)

                // 2️⃣ For each match, get ward information

                // 3️⃣ Save ward data for AnalyticsScreen
                _analyticsData.value = wardmap

                val reportHistory = playerAccountId.personaname?.let {
                    playerAccountId.avatarfull?.let { it1 ->
                        PlayerReport(
                            steamId = playerAccountId.account_id,
                            profileName = it,
                            profilePicUrl = it1,
                            wardMapJson = wardmap
                        )
                    }
                }

                if (reportHistory != null) {
                    repository.insertPlayerReport(reportHistory)
                }
                else{
                    Log.d("SearchViewModel","reportHistory is null")
                }

            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error fetching ward data", e)
            } finally {
                _isReportLoading.value = false
            }
        }
    }
}

