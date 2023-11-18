package com.kelompok5.adoptmenow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kelompok5.adoptmenow.account.LoginFragmentDirections

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.main_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        if (Firebase.auth.currentUser != null) {
            navController.navigate(
                LoginFragmentDirections
                    .actionLoginFragmentToMainFragment())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.main_nav_host_fragment)
        return NavigationUI.navigateUp(navController, null)
    }
}