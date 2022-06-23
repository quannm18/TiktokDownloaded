package com.example.tiktokdownloaded.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val bottomView: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.bottomView) }
    private lateinit var navControl : NavController;
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentMain) as NavHostFragment

        navControl = navHostFragment.navController;
        setupWithNavController(bottomView, navControl)



    }
}