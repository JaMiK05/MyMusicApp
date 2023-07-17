package uz.gita.music_app_jamik.presentation.splash

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.flow.onEach
import uz.gita.music_app_jamik.R
import uz.gita.music_app_jamik.util.MyEventBus
import uz.gita.music_app_jamik.util.checkPermision
import uz.gita.music_app_jamik.util.getMusicCursor

/**
 *   Created by Jamik on 7/15/2023 ot 12:01 PM
 **/

class SplashScreen : AndroidScreen() {

    @SuppressLint("UseCheckPermission")
    @Composable
    override fun Content() {
        val viewmodel: SplashContract.ViewModel = getViewModel<SplashViewModel>()
        val loading by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_dots))

        val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_loading))
        val context = LocalContext.current

        if (viewmodel.uiState.collectAsState().value.request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.checkPermision(
                    arrayListOf(
                        android.Manifest.permission.POST_NOTIFICATIONS,
                        android.Manifest.permission.READ_MEDIA_AUDIO
                    )
                ) {
                    viewmodel.onEventDispatcher(SplashContract.Intent.NextScreen(context = context))
                }
            } else {
                context.checkPermision(
                    arrayListOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    viewmodel.onEventDispatcher(SplashContract.Intent.NextScreen(context = context))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .padding(top = 200.dp)
                    .size(150.dp),
                painter = painterResource(id = R.drawable.silver_guitar),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = "Gita IT Acedemy Productions",
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = Color(context.getColor(R.color.silver))
            )
            LottieAnimation(
                composition = splash,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(200.dp),
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier.height(30.dp),
                    text = "Loading",
                    color = Color(context.getColor(R.color.silver))
                )
                LottieAnimation(
                    composition = loading,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .height(20.dp)
                        .width(50.dp),
                    alignment = Alignment.BottomStart
                )
            }

        }
    }

    @Preview
    @Composable
    private fun PrevSplash() {
        MaterialTheme() {
            Surface(modifier = Modifier.fillMaxSize()) {
                Content()
            }
        }
    }
}