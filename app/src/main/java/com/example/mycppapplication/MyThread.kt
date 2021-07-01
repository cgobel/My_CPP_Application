package com.example.mycppapplication

import kotlinx.android.synthetic.main.activity_main.*

class MyThread : Thread() {
    var listeners : MutableList<MyEventListener> = ArrayList()
    var enabled = false

    fun addListener(listener : MyEventListener){
        listeners.add(listener)
    }

    fun removeListener(listener : MyEventListener){
        if (listeners.contains(listener)){
                listeners.remove(listener)
        }
    }



    override fun run() {
        if (!enabled) {
            //enableAccelerometer();
            //sample_text.text = stringFromJNI()
            enabled = true
        }

       // disableAccelerometer()
    }


}