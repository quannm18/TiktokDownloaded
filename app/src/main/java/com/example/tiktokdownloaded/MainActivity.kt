package com.example.tiktokdownloaded

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.tiktokdownloaded.adapter.VPAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val bottomView: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottomView) }
    private lateinit var navControl : NavController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain) as NavHostFragment

        navControl = navHostFragment.navController;

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomView)
        setupWithNavController(bottomView, navControl)
    }
}