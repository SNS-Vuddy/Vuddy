# 학습

- API 통신 구현


## 초기 설정

### Gradle JDK

- corretto 17

### build.gradle(project)

```gradle
plugins {
    id 'com.android.application' version '8.0.0' apply false
    id 'com.android.library' version '8.0.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
}
```

### build.gradle(app)

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.hyunseokcheong.httptest'
    compileSdk 33

    defaultConfig {
        applicationId "com.hyunseokcheong.httptest"
        minSdk 26
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    // corretto - 17
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    // corretto - 17
    kotlinOptions {
        jvmTarget = '17'
    }

    // data binding
    viewBinding {
        enabled = true
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.10.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    // retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
```

### androidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- 인터넷 -->
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.HttpTest"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

## 구현

### Mainactivity.kt

```kotlin
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
```

### AirQualityService.kt

```kotlin
package com.hyunseokcheong.httptest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityService {
    
    @GET("nearest_city")
    fun getAirQualityDate(@Query("lat") lat: String, @Query("lon") lon: String, @Query("key") key: String): Call<AirQualityResponse>
}
```

### AirQualityResponse.kt

```kotlin
package com.hyunseokcheong.httptest

data class AirQualityResponse(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val city: String,
        val country: String,
        val current: Current,
        val location: Location,
        val state: String
    ) {
        data class Current(
            val pollution: Pollution,
            val weather: Weather
        ) {
            data class Pollution(
                val aqicn: Int,
                val aqius: Int,
                val maincn: String,
                val mainus: String,
                val ts: String
            )
            
            data class Weather(
                val hu: Int,
                val ic: String,
                val pr: Int,
                val tp: Int,
                val ts: String,
                val wd: Int,
                val ws: Double
            )
        }
        
        data class Location(
            val coordinates: List<Double>,
            val type: String
        )
    }
}
```

### RetrofitConnection.kt

```kotlin
package com.hyunseokcheong.httptest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    
    companion object {
        private const val BASE_URL = "https://api.airvisual.com/v2/"
        private var INSTANCE: Retrofit? = null
        
        fun getInstance(): Retrofit {
            
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            
            return INSTANCE!!
        }
    }
}
```