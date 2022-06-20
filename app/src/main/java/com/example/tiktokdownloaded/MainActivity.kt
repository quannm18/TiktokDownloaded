package com.example.tiktokdownloaded

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log.d
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.tiktokdownloaded.adapter.VPAdapter
import com.example.tiktokdownloaded.repository.Repository
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.logging.Logger

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