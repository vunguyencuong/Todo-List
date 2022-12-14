package com.example.todoapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity() {

    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        actionBar = supportActionBar!!
        actionBar.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setUpAnimation()
    }


    private fun setUpAnimation() {
        var postDelayed = Handler().postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 1000)
    }

}