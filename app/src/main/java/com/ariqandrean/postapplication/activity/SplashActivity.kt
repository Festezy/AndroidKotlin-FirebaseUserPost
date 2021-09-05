package com.ariqandrean.postapplication.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.ariqandrean.postapplication.R
import com.ariqandrean.postapplication.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var imageView: ImageView
    private lateinit var textViewApp: TextView
    private lateinit var textViewName: TextView
    private lateinit var auth: FirebaseAuth
    private val animationTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        imageView = binding.splashImageView
        textViewApp = binding.textViewapp
        textViewName = binding.textViewName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val animatorY: ObjectAnimator= ObjectAnimator.ofFloat(imageView, "y", 500f)
        val animatorX: ObjectAnimator= ObjectAnimator.ofFloat(textViewApp, "x", 200f)

        animatorY.duration = animationTime
        animatorX.duration = animationTime

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorY, animatorX)
        animatorSet.start()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        },4000)
    }
}