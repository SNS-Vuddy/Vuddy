package com.example.kakaologin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kakaologin.ui.theme.KakaoLoginTheme

class MainActivity : ComponentActivity() {

    private val kakaoAuthViewModel : KakaoAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KakaoLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KakaoLoginView(kakaoAuthViewModel)
                }
            }
        }
    }
}

@Composable
fun KakaoLoginView(viewModel: KakaoAuthViewModel) {

    val isLoggedIn = viewModel.isLoggedIn.collectAsState()

    val loginStatusInfoTitle = if (isLoggedIn.value) "로그인" else "로그아웃"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            viewModel.kakaoLogin()
        }) {
            Text(text = "카카오 로그인")
        }
        Button(onClick = {
            viewModel.kakaoLogout()
        }) {
            Text(text = "카카오 로그아웃")
        }
        Text(text = loginStatusInfoTitle, textAlign = TextAlign.Center, fontSize = 20.sp)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KakaoLoginTheme {
        Greeting("Android")
    }
}