package com.classic.dota2wardvision.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classic.dota2wardvision.alarm.AlarmEntity
import com.classic.dota2wardvision.alarm.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repository: AlarmRepository
) : ViewModel() {

    val alarms = repository.getAllAlarms()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addAlarm(
        startMinute: Int,
        startSecond: Int,
        repeatMinute: Int,
        repeatSecond: Int,
        text: String
    ) {
        viewModelScope.launch {
            repository.insertAlarm(
                AlarmEntity(
                    startMinute = startMinute,
                    startSecond = startSecond,
                    repeatMinute = repeatMinute,
                    repeatSecond = repeatSecond,
                    alarmText = text
                )
            )
        }
    }

    fun addAlarmEntity(
        alarm: AlarmEntity
    ) {
        viewModelScope.launch {
            repository.insertAlarm(
                alarm
            )
        }
    }

    fun deleteAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }
}
