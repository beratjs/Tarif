package com.android.tarif.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.android.tarif.R
import kotlinx.android.synthetic.main.activity_splash.*
import pl.droidsonroids.gif.GifImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        findViewById<GifImageView>(R.id.splash);
        splash.startAnimation(anim);

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3800)
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}