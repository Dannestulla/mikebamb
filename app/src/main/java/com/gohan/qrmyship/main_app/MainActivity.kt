package com.gohan.qrmyship.main_app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gohan.qrmyship.R
import com.gohan.qrmyship.main_app.domain.myConstants
import com.gohan.qrmyship.main_app.domain.myConstants.CAN_EDIT
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navController = findNavController(R.id.fragment)
        setupBottomNavMenu(navController)
        setDefaultUserEdit()
    }

    private fun setDefaultUserEdit() {
        val sharedPref =
            this.getSharedPreferences(myConstants.SHARED_PREF, Context.MODE_PRIVATE)!!
        sharedPref.edit().putBoolean(CAN_EDIT, false).apply()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }
}
