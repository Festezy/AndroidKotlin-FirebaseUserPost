package com.ariqandrean.postapplication.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ariqandrean.postapplication.databinding.ActivityCreateProfileBinding
import com.ariqandrean.postapplication.model.MyProfileModel
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.HashMap

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private var imageUri: Uri? = null
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database
        firestore = Firebase.firestore
        storage = Firebase.storage
        auth = Firebase.auth

        val myProfile = MyProfileModel()
        val CircleImageView: CircleImageView = binding.createProfileImageView
        val nameEditText: EditText = binding.createProfileNameEditText
        val bioEditText: EditText = binding.createProfileBioEditText
        val emailEditText: EditText = binding.createProfileEmailEditText
        val createTextView: TextView = binding.createProfileCREATETextView
        val progressBar: ProgressBar = binding.createProfileProgressBar

        var currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let {
            uid = it.uid
        }

        val resultLauncher: ActivityResultLauncher<Intent> =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try {
                if (it?.resultCode == Activity.RESULT_OK){
                    it.data?.let {
                        imageUri = it.data
                        Glide.with(this).load(imageUri).into(CircleImageView)
                    }
                }
            } catch (e: Exception){
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        CircleImageView.setOnClickListener{
            val intent = Intent().apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            resultLauncher.launch(intent)
        }

        createTextView.setOnClickListener{
            createTextView.setBackgroundColor(Color.BLUE)

            val name = nameEditText.toString()
            val bio = bioEditText.text.toString()
            val email = emailEditText.text.toString()

            if (name.isNotEmpty() && bio.isNotEmpty() && email.isNotEmpty()){
                progressBar.visibility = View.VISIBLE
                imageUri?.let { it ->
                    val contentResolver: ContentResolver = contentResolver
                    val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
                    val fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(it))

                    val reference = storage.getReference("profile_images").child(System.currentTimeMillis().toString() + ".$fileExtension")
                    val uploadTask = reference.putFile(it)
                    uploadTask.continueWithTask{
                        if (!it.isSuccessful) {
                            throw it.exception!!.cause!!
                        }
                        reference.downloadUrl
                    }.addOnCompleteListener {
                        if (it.isSuccessful){
                            it.result?.let {
                                val downloadUri = it
                                val profileMap = HashMap<String, String>()
                                profileMap.put("name", name)
                                profileMap.put("bio", bio)
                                profileMap.put("email", email)
                                profileMap.put("url", downloadUri.toString())
                                profileMap.put("uid", uid!!)

                                myProfile.name = name
                                myProfile.uri = downloadUri.toString()
                                myProfile.uid = uid

                                uid?.let {
                                    database.getReference("users").child(it).setValue(myProfile)
                                    firestore.collection("users").document(it).set(profileMap).addOnSuccessListener {
                                        progressBar.visibility = View.INVISIBLE
                                        Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show()

                                        val handler = Handler(Looper.getMainLooper())
                                        handler.postDelayed({
                                            val intent = Intent(this, MyProfileActivity::class.java)
                                            startActivity(intent)
                                        }, 2000)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}