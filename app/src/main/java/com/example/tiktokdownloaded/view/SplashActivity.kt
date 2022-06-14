package com.example.tiktokdownloaded.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.example.tiktokdownloaded.MainActivity
import com.example.tiktokdownloaded.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val cdtm : CountDownTimer = object : CountDownTimer(3000,3000){
            override fun onTick(p0: Long) {
                TODO("Not yet implemented")
            }

            override fun onFinish() {
                val intent : Intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }
}