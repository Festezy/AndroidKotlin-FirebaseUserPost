package com.ariqandrean.postapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.ariqandrean.postapplication.R
import com.ariqandrean.postapplication.databinding.ActivityMainBinding
import com.ariqandrean.postapplication.fragment.BottomMenuFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuImageButton = binding.mainImageButton

        menuImageButton.setOnClickListener{
            val bottomMenu = BottomMenuFragment()
            bottomMenu.show(supportFragmentManager, "bottomSheet")
        }
    }
}