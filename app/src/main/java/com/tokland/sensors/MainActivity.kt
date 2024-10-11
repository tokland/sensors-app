package com.tokland.sensors

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("[println] Hello World from Kotlin!")

        val fileOutputStream: FileOutputStream = openFileOutput("out-tokland.txt", MODE_PRIVATE)
        fileOutputStream.write(("line1" + "\n").toByteArray())
        fileOutputStream.write(("line2" + "\n").toByteArray())
        fileOutputStream.close()

        Log.d("MainActivity", "[Log.d] Hello World from Kotlin!")
        finish()
    }
}
