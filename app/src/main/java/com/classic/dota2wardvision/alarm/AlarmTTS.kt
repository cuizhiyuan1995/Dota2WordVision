package com.classic.dota2wardvision.alarm

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class AlarmTTS(context: Context) {
    private var tts: TextToSpeech? = null

    fun setupTTS(context: Context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                // Optional: select a specific voice
                // val voice = tts.voices.find { it.name.contains("en-us") }
                // tts.voice = voice
            }
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
