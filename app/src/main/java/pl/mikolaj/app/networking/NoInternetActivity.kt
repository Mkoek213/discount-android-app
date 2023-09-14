package pl.mikolaj.app.networking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.mikolaj.app.R

class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
    }
}