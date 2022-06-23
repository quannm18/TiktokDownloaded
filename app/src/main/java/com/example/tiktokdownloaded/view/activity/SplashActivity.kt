package com.example.tiktokdownloaded.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.tiktokdownloaded.R

class SplashActivity : AppCompatActivity() {
    private val imgLogoSplash: ImageView by lazy { findViewById<ImageView>(R.id.imgLogoSplash) }
    private val tvLogoSplash: TextView by lazy { findViewById<TextView>(R.id.tvLogoSplash) }
    private val tvSubTitleRow: TextView by lazy { findViewById<TextView>(R.id.tvSubTitleRow) }
    private val tvSubTitleRow1: TextView by lazy { findViewById<TextView>(R.id.tvSubTitleRow1) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val animFadein : Animation= AnimationUtils.loadAnimation(applicationContext,R.anim.text_alpha_anim);


        imgLogoSplash.startAnimation(animFadein);
        tvLogoSplash.startAnimation(animFadein);
        tvSubTitleRow.startAnimation(animFadein);
        tvSubTitleRow1.startAnimation(animFadein);
        val cdtm : CountDownTimer = object : CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val intent : Intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}