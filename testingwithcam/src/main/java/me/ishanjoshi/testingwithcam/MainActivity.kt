package me.ishanjoshi.testingwithcam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

                startActivity(Intent(this, BarChartTest::class.java))
//        startActivity(Intent(this, PieChartTest::class.java))
    }
}
