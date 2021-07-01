package com.example.mycppapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Thread.interrupted

const val TIMEOUT = 10 // seconds
const val TIMEOUT_MS = TIMEOUT*1000 // milliseconds

class MainActivity : AppCompatActivity(), SensorEventListener {
    var counter = 0
    var enabled = false
    var inter = false
    var starttime : Long = 0
    lateinit var event : SensorEvent
    var threadStarted = false
    lateinit var myThread : Thread
    //var sensorManager : SensorManager
    //var accel : Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //val accel : Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //sensorManager.registerListener(this, accel, 1000000/500, 0)
        //enabled = true
        //starttime = System.currentTimeMillis()

        //val status = enableAccelerometer()
        //sample_text.text = ("enabled "+status)
        // Example of a call to a native method


        //sample_text.text = ("Counter:"+ counter + " nullCounter:"+nullCounter)
    }

    fun onGetClick(view : View?) {
        enableAccelerometer()
        enabled = true
        myThread = Thread {
            var counterr = 0
            try {
                while (!Thread.currentThread().isInterrupted) {
                    //print values every second
                    val array = getSensorEvent();
                    sample_text.post {
                        sample_text.text = "x: %.4f y: %.4f z: %.4f".format(array[0], array[1], array[2])
                    }
                    Thread.sleep(1000)
                }
            }catch(e : InterruptedException){
                disableAccelerometer()
            }
        }
        myThread.start()
    }

    fun onStopClick(view : View?){
        //inter = true
        myThread.interrupt()
        enabled = false
        //sensorManager.unregisterListener(this)
        sample_text.text = ("diaabled")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //we dont use this function in this case
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(enabled){

            if(System.currentTimeMillis() - starttime > 10000){
                println("now we printed")
                sample_text.text = ("count nach 10s: $counter")
                counter = 0
                starttime = System.currentTimeMillis()
            }
            //
            counter++;
        }
    }
    external fun enableAccelerometer() : Int
    external fun disableAccelerometer() : Int
    external fun getSensorEvent() : DoubleArray
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
