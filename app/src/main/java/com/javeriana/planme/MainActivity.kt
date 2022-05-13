package com.javeriana.planme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity(R.layout.activity_main) {

	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val navHostFragment = supportFragmentManager
			.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController

		val appBarConfiguration =
			AppBarConfiguration(
				setOf(
					R.id.loginFragment,
					R.id.popularPlansFragment,
					R.id.planMeFragment,
					R.id.searchPlanFragment
				)
			)
		setupActionBarWithNavController(navController, appBarConfiguration)
	}

	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp() || super.onSupportNavigateUp()
	}
}
