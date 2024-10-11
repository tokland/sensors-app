package com.tokland.sensors

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("[println] Hello World from Kotlin!")
        Log.d("HelloWorldCmdApp", "[Log.d] Hello World from Kotlin!")
        finish()
    }
}
