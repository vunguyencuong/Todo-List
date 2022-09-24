package com.example.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private  lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
      //  setContentView(R.layout.activity_main)
        setContentView(binding.root)
        setNavigationBar()

    }


    private fun setNavigationBar(){
        navController = binding.navHostFragment.getFragment<NavHostFragment>().navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.taskFragment,
                R.id.addFragment,
                R.id.updateFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

}