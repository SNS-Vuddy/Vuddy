package com.hyunseokcheong.httptest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyunseokcheong.httptest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setButton()
    }
    
    private fun setButton() {
        binding.btn.setOnClickListener {
            //35.160191,129.1612369
            getAirQualityData("35.160191", "129.1612369")
        }
    }
    
    private fun getAirQualityData(latitude: String, longitude: String) {
        
        var retrofitAPI = RetrofitConnection.getInstance().create(AirQualityService::class.java)
        val apiKey = "99db7ba4-eb75-4406-a584-67625df9278b"
        
        retrofitAPI.getAirQualityDate(latitude, longitude, apiKey)
            .enqueue(object : Callback<AirQualityResponse> {
                // 성공
                override fun onResponse(call: Call<AirQualityResponse>, response: Response<AirQualityResponse>) {
                    if (response.isSuccessful) {
                        // response
                        response.body()?.data?.city?.let {
                            Toast.makeText(this@MainActivity, "성공 $it", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "실패1", Toast.LENGTH_LONG).show()
                    }
                }
                
                // 실패
                override fun onFailure(call: Call<AirQualityResponse>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@MainActivity, "실패2", Toast.LENGTH_LONG).show()
                }
            })
    }
}