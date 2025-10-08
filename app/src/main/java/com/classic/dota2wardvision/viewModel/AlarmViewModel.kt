package com.classic.dota2wardvision.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classic.dota2wardvision.alarm.AlarmEntity
import com.classic.dota2wardvision.alarm.AlarmRepository
import com.classic.dota2wardvision.alarm.AlarmTTS
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val alarms = repository.getAllAlarms()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _timeInSeconds = MutableStateFlow(0)
    val timeInSeconds: StateFlow<Int> = _timeInSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val alarmTTS = AlarmTTS(appContext)

    private var timerJob: Job? = null

//    init {
//        viewModelScope.launch {
////            _isRunning.collect { running ->
////                if (running) {
////                    while (running) {
////                        Log.d("AlarmViemodel_running",running.toString())
////                        delay(1000L)
////                        _timeInSeconds.value += 1
////                        // your alarm logic here
////                        checkAlarms(_timeInSeconds.value)
////                    }
////                }
////            }
//            if(_isRunning.value){
//                while(true) {
//                    Log.d("AlarmViemodel_running",_isRunning.value.toString())
//                    delay(1000L)
//                    _timeInSeconds.value += 1
//                    // your alarm logic here
//                    checkAlarms(_timeInSeconds.value)
//                }
//            }
//        }
//    }

    init {
        alarmTTS.setupTTS(appContext)
    }

    fun startAlarm() {
        if (timerJob?.isActive == true) return // already running

        timerJob = viewModelScope.launch {
            while (isActive) {    // checks coroutine cancellation
                delay(1000L)
                _timeInSeconds.value += 1
                checkAlarms(_timeInSeconds.value)
            }
        }
    }

//    fun pauseAlarm() {
//        _isRunning.value = false
//    }

    fun pauseAlarm() {
        timerJob?.cancel()
        timerJob = null
    }

//    fun stopAlarm() {
//        _isRunning.value = false
//        _timeInSeconds.value = 0
//    }

    fun stopAlarm() {
        timerJob?.cancel()
        timerJob = null
        _timeInSeconds.value = 0
    }

    fun adjustAlarm(adjustTime:Int) {
        val newValue = _timeInSeconds.value + adjustTime
        _timeInSeconds.value = maxOf(newValue, 0)
    }

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

    private fun checkAlarms(timeInSeconds: Int) {
        val alarmList = alarms.value   // current list from StateFlow
        alarmList.forEach { alarm ->
            val repeatTime = alarm.repeatMinute * 60 + alarm.repeatSecond
            val startTime = alarm.startMinute * 60 + alarm.startSecond
            if (repeatTime > 0) {
                if ((timeInSeconds - startTime) >= 0 &&
                    (timeInSeconds - startTime) % repeatTime == 0
                ) {
                    alarmTTS.speak(alarm.alarmText)
                    Log.d("⏰ Alarm Triggered", alarm.alarmText)
                }
            } else {
                if (timeInSeconds == startTime) {
                    alarmTTS.speak(alarm.alarmText)
                    Log.d("⏰ Alarm Triggered Once", alarm.alarmText)
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        alarmTTS.shutdown()
    }

}
