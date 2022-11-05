package io.github.izm_o.umbrellaapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.izm_o.umbrellaapp.databinding.ActivityDestinationBinding

class DestinationActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityDestinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)
        //viewBinding
        binding = ActivityDestinationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //AlarmReceiver.ktから値を受け取って表示する
        val sharedPref = getSharedPreferences(
            "DataStore3", Context.MODE_PRIVATE)
        val rain1 = sharedPref.getString("InputRain1","")
        val rain2 = sharedPref.getString("InputRain2","")
        val rain3 = sharedPref.getString("InputRain3","")
        val rain4 = sharedPref.getString("InputRain4","")

        binding.tv1.text = rain1
        binding.tv2.text = rain2
        binding.tv3.text = rain3
        binding.tv4.text = rain4
    }
}