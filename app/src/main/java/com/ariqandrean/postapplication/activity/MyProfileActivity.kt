package com.ariqandrean.postapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.ariqandrean.postapplication.databinding.ActivityMyProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var backImageButton: ImageButton
    private lateinit var editImageButton: ImageButton
    private lateinit var circleImageView: CircleImageView
    private lateinit var nameTextView: TextView
    private lateinit var bioTextView: EditText
    private lateinit var emailEditText: EditText
//    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        backImageButton = binding.myProfileBackImageButton
        editImageButton = binding.myProfileEditImageButton
        circleImageView = binding.myprofileCircleImageView
        nameTextView = binding.myprofileNameTextView
        bioTextView = binding.myProfileBioEditText
        emailEditText = binding.myProfileEmailEditText

        backImageButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
//        currentUser?.let { it ->
//            val uid = it.uid
////            val reference = firestore.collection("users").document(uid)
//
//            reference.get().addOnCompleteListener{ it ->
//                it.result?.let{
//                    if (it.exists()) {
//                        val name = it.getString("name")
//                        val bio = it.getString("bio")
//                        val email = it.getString("email")
//                        val url = it.getString("url")
//
//                        Glide.with(this).load(url).into(circleImageView)
//                        nameTextView.text = name
//                        bioTextView.text = bio
//                        emailEditText.text = email
//                    } else {
//                        val intent = Intent(this, CreateProfileActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            }
//        }
    }
}