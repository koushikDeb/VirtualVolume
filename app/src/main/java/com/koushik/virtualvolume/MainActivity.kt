package com.koushik.virtualvolume

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Switch

class MainActivity : AppCompatActivity() {


     lateinit var enable_switch:Switch;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enable_switch =findViewById(R.id.enable_switch)

        enable_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            var i =  Intent(this,Volumeservice::class.java);
            if(isChecked)
            {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Volumeservice.startService(this, "Foreground Service is running...")
                }
                else
                {
                    Volumeservice.startService(this, "Foreground Service is running...")

                }
            }
            else
            {
                Volumeservice.stopService(this)
            }
        }
    }
}
