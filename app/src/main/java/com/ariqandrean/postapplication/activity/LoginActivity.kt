package com.ariqandrean.postapplication.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import com.ariqandrean.postapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var showPassCheckBox: CheckBox
    private lateinit var loginTextView: TextView
    private lateinit var registerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        emailEditText = binding.loginEmailEditText
        passwordEditText = binding.loginPasswordEditText
        showPassCheckBox = binding.loginShowCheckBox
        loginTextView = binding.loginLoginTextView
        registerTextView = binding.loginRegisterTextView
        progressBar = binding.loginprogressBar

        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        showPassCheckBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        loginTextView.setOnClickListener {
            loginTextView.setBackgroundColor(Color.BLUE)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val error = it.exception
                            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                progressBar.visibility = View.INVISIBLE
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            registerTextView.setBackgroundColor(Color.RED)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}