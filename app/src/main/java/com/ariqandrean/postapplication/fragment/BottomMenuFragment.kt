package com.ariqandrean.postapplication.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ariqandrean.postapplication.activity.LoginActivity
import com.ariqandrean.postapplication.databinding.BottomMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BottomMenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomMenuBinding
    private lateinit var profileLinear: LinearLayout
    private lateinit var logoutLinear: LinearLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomMenuBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        profileLinear = binding.bottommenuProfile
        logoutLinear = binding.bottommenuLogout

        profileLinear.setOnClickListener {
//            val intent = Intent(requireActivity(), ProfileActivity::class.java)
//            startActivity(intent)
        }

        logoutLinear.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity()).apply {
                setTitle("Logout")
                setMessage("Are you sure to Logout?")
                setPositiveButton("Logout") { _, _ ->
                    auth.signOut()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                }
                setNegativeButton("Cancel") { _, _ -> }
            }
            builder.create()
            builder.show()
        }
        return binding.root
    }
}