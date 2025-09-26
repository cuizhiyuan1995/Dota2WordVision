package com.classic.dota2wardvision.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classic.dota2wardvision.openDotAAPI.WardMapResponse
import com.classic.dota2wardvision.room.PlayerReport
import com.classic.dota2wardvision.room.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: PlayerRepository
) : ViewModel() {

    val reports: StateFlow<List<PlayerReport>> = repository.allReports
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    // For navigation
    private val _selectedReport = MutableStateFlow<WardMapResponse?>(null)
    val selectedReport: StateFlow<WardMapResponse?> = _selectedReport.asStateFlow()

    fun selectReport(report: WardMapResponse) {
        _selectedReport.value = report
    }

}
