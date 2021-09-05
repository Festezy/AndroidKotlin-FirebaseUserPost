package com.ariqandrean.postapplication.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import com.ariqandrean.postapplication.R
import com.ariqandrean.postapplication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordsEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var showCheckBox: CheckBox
    private lateinit var registerTextView: TextView
    private lateinit var loginTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        emailEditText = binding.registerEmailEditText
        passwordsEditText = binding.registerPasswordEditText
        confirmPasswordEditText = binding.registerConfirmPasswordEditText
        showCheckBox = binding.registerShowCheckBox
        registerTextView = binding.registerRegisterTextView
        loginTextView = binding.registerLoginTextView
        progressBar = binding.registerProgressBar

        passwordsEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        showCheckBox.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                passwordsEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                confirmPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordsEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        registerTextView.setOnClickListener {
            registerTextView.setBackgroundColor(Color.BLUE)
            val email = emailEditText.text.toString()
            val password = passwordsEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                val error = it.exception
                                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    progressBar.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginTextView.setOnClickListener {
            loginTextView.setBackgroundColor(Color.CYAN)
            val intent = Intent(this, LoginActivity::class.java)
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