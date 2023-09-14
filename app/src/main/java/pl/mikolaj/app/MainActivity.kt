package pl.mikolaj.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.mikolaj.app.fragments.HomeFragment
import pl.mikolaj.app.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        settingsFragment.setLanguageChangeListener(homeFragment)

    }
}