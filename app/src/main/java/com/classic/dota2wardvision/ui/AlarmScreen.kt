package com.classic.dota2wardvision.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.classic.dota2wardvision.alarm.AlarmEntity
import com.classic.dota2wardvision.alarm.AlarmTTS
import com.classic.dota2wardvision.viewModel.AlarmViewModel
import kotlinx.coroutines.delay

@Composable
fun AlarmScreen(
    navController: NavHostController,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    val alarms by viewModel.alarms.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val alarmTTS = remember { AlarmTTS(context) }

    LaunchedEffect(Unit) {
        alarmTTS.setupTTS(context)
    }

    // Coroutine to update timer every second when running
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000L)
                timeInSeconds++


                // Check alarms
                alarms.forEach { alarm ->
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
        }
    }

    // Clean up when Composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            alarmTTS.shutdown()
        }
    }

    // Format as MM:SS (minutes can exceed 60)
    val formattedTime = String.format(
        "%02d:%02d",
        timeInSeconds / 60,
        timeInSeconds % 60
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Timer text
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons row
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Pause button
            Button(onClick = { isRunning = false }) {
                Text("Pause")
            }

            // Start button
            Button(onClick = { isRunning = true }) {
                Text("Start")
            }

            // Reset button
            Button(onClick = {
                isRunning = false
                timeInSeconds = 0
            }) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row 2: Adjustment buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
//            Button(onClick = {
//                timeInSeconds = (timeInSeconds - 120).coerceAtLeast(0)
//            }) {
//                Text("-2m")
//            }
//
//            Button(onClick = {
//                timeInSeconds = (timeInSeconds - 15).coerceAtLeast(0)
//            }) {
//                Text("-15s")
//            }
//
//            Button(onClick = {
//                timeInSeconds = (timeInSeconds - 1).coerceAtLeast(0)
//            }) {
//                Text("-1s")
//            }
//
//            Button(onClick = { timeInSeconds += 1 }) {
//                Text("+1s")
//            }
//
//            Button(onClick = { timeInSeconds += 15 }) {
//                Text("+15s")
//            }
//
//            Button(onClick = { timeInSeconds += 120 }) {
//                Text("+2m")
//            }
            listOf(
                "-2m" to { timeInSeconds = (timeInSeconds - 120).coerceAtLeast(0) },
                "-15s" to { timeInSeconds = (timeInSeconds - 15).coerceAtLeast(0) },
                "-1s" to { timeInSeconds = (timeInSeconds - 1).coerceAtLeast(0) },
            ).forEach { (label, action) ->
                Button(
                    onClick = action,
                    modifier = Modifier.weight(1f) // share width equally
                ) {
                    Text(
                        text = label,
                        //fontSize = 2.sp, // smaller font
                        maxLines = 1
                    )
                }
            }
        }

        //continue adjustment buttons
        // Row 2: Adjustment buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "+1s" to { timeInSeconds += 1 },
                "+15s" to { timeInSeconds += 15 },
                "+2m" to { timeInSeconds += 120 }
            ).forEach { (label, action) ->
                Button(
                    onClick = action,
                    modifier = Modifier.weight(1f) // share width equally
                ) {
                    Text(
                        text = label,
                        //fontSize = 2.sp, // smaller font
                        maxLines = 1
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        //edit
        // Alarm rows
        Text("Alarms:", style = MaterialTheme.typography.titleMedium)
        alarms.forEachIndexed { index, alarm ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp) // padding inside the border
            ){
                AlarmRow(
                    alarm = alarm,
                    onUpdate = { updated ->
                        viewModel.addAlarmEntity(updated)
                    },
                    onDelete = {
                        viewModel.deleteAlarm(alarm)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add alarm button
        Button(onClick = {
            viewModel.addAlarm(
                0,
                0,
                0,
                0,
                "")
        }) {
            Text("➕ Add Alarm")
        }
    }
}

@Composable
fun AlarmRow(
    alarm: AlarmEntity,
    onUpdate: (AlarmEntity) -> Unit,
    onDelete: () -> Unit,
) {
    val minutesRange = (0..60).toList()
    val secondsRange = (0..60).toList()

    val startMinutes = alarm.startMinute
    val startSeconds = alarm.startSecond
    val repeatMinutes = alarm.repeatMinute
    val repeatSeconds = alarm.repeatSecond

    Column(
        modifier = Modifier.fillMaxWidth(),
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Start time pickers
            DropdownSelector(
                label = "Start Minute",
                options = minutesRange,
                selected = startMinutes,
                onSelect = { min ->
                    onUpdate(alarm.copy(startMinute = min))
                },
                modifier = Modifier.weight(1f)
            )
            DropdownSelector(
                label = "Start Second",
                options = secondsRange,
                selected = startSeconds,
                onSelect = { sec ->
                    onUpdate(alarm.copy(startSecond = sec))
                },
                modifier = Modifier.weight(1f)
            )


        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Repeat time pickers
            DropdownSelector(
                label = "Repeat per Minute",
                options = minutesRange,
                selected = repeatMinutes,
                onSelect = { min ->
                    onUpdate(alarm.copy(repeatMinute = min))
                },
                modifier = Modifier.weight(1f)
            )
            DropdownSelector(
                label = "Repeat per Second",
                options = secondsRange,
                selected = repeatSeconds,
                onSelect = { sec ->
                    onUpdate(alarm.copy(repeatSecond = sec))
                },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Alarm text
            OutlinedTextField(
                value = alarm.alarmText,
                onValueChange = { onUpdate(alarm.copy(alarmText = it)) },
                label = { Text("Text") },
                modifier = Modifier.weight(2f)
            )

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

//// Helpers
//fun secondsToMMSS(seconds: Int): String =
//    String.format("%02d:%02d", seconds / 60, seconds % 60)
//
//fun mmssToSeconds(input: String): Int {
//    return try {
//        val parts = input.split(":")
//        val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
//        val secs = parts.getOrNull(1)?.toIntOrNull() ?: 0
//        minutes * 60 + secs
//    } catch (e: Exception) {
//        0
//    }
//}

