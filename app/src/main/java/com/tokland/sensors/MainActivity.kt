package com.tokland.sensors

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tokland.sensors.ui.theme.SensorsTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var rotationVectorSensor: Sensor? = null
    private var _angle by mutableStateOf(0.0) // Declare a state for the angle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val serviceIntent = Intent(this, MyForegroundService::class.java)
        startService(serviceIntent)

        // Initialize SensorManager and Sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        // Register the sensor listener
        rotationVectorSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Set up the composable UI
        setContent {
            SensorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pass the dynamic angle value to the Greeting composable
                    Greeting(
                        name = "Android, Angle: %.1f".format(_angle),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        this.torchToggleButton(true)
        
        // Debug print statement to mark the end of onCreate
        println("tokland:debug:end")
    }

    fun torchToggleButton(state: Boolean) {
        // Control the torch using CameraManager
        val cameraManager = getSystemService(CameraManager::class.java)
        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id).get(
                android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
            ) == true
        }

        cameraId?.let {
            cameraManager.setTorchMode(it, state)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            // Get rotation matrix from the sensor event
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            // Get the device orientation in radians
            val orientationValues = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationValues)

            // Convert pitch (X-axis rotation) from radians to degrees
            val pitchInDegrees = toDegrees(orientationValues[1].toDouble())
            _angle = pitchInDegrees

            // Print the X-axis angle (pitch) to the console
            println("tokland:debug:X-axis angle X: %.1f".format(pitchInDegrees))

            // Example: Control torch based on angle (if angle > 45 degrees)
            if (pitchInDegrees > 45) {
                //this.torchToggleButton(true)
            } else {
                //this.torchToggleButton(false)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if needed
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener when the activity is paused
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

fun toDegrees(radians: Double): Double {
    return radians * 180 / Math.PI
}
