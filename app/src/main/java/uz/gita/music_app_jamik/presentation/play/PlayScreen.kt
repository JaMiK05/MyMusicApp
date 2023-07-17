package uz.gita.music_app_jamik.presentation.play

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import io.github.ningyuv.circularseekbar.CircularSeekbarView
import uz.gita.music_app_jamik.R
import uz.gita.music_app_jamik.data.model.CommandEnum
import uz.gita.music_app_jamik.data.model.SpeedEnum
import uz.gita.music_app_jamik.ui.componenta.CircularSeekBar
import uz.gita.music_app_jamik.ui.componenta.PlayMusicItem
import uz.gita.music_app_jamik.util.MyEventBus
import uz.gita.music_app_jamik.util.getMusicDataByPosition
import uz.gita.music_app_jamik.util.setVolume
import uz.gita.music_app_jamik.util.startMusicService
import java.util.concurrent.TimeUnit


/**
 *   Created by Jamik on 7/15/2023 ot 2:03 PM
 **/

class PlayScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: PlayContract.ViewModel = getViewModel<PlayViewModel>()
        PlayContent(onEvenDispatcher = viewModel::onEvenDispatcher)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PlayContent(
        onEvenDispatcher: ((PlayContract.Intent) -> Unit)? = null,
    ) {
        val cn = LocalContext.current

        if (MyEventBus.message.collectAsState().value.isNotEmpty()){
            Toast.makeText(cn, MyEventBus.message.collectAsState().value, Toast.LENGTH_SHORT).show()
        }

        val scrollState = rememberScrollState()
        var shouldAnimated by remember { mutableStateOf(true) }

        var musicScore by remember { mutableStateOf(false) }

        var currentTime by remember { mutableStateOf("0:00") }

        var musicTime by remember { mutableStateOf("0:00") }

        val musicData = MyEventBus.currentMusicData.collectAsState()

        if (MyEventBus.currentTime.collectAsState().value != 0) {
            val milliseconds = MyEventBus.currentTime.collectAsState().value.toLong()
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minutes = (milliseconds / 1000 / 60) % 60
            val seconds = (milliseconds / 1000) % 60
            currentTime = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
            else "%02d:%02d:%02d".format(hours, minutes, seconds)
        }

        if (MyEventBus.musicTime.collectAsState().value != 0) {
            val milliseconds = MyEventBus.musicTime.collectAsState().value.toLong()
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minutes = (milliseconds / 1000 / 60) % 60
            val seconds = (milliseconds / 1000) % 60
            musicTime = if (hours == 0L) "%02d:%02d".format(minutes, seconds)
            else "%02d:%02d:%02d".format(hours, minutes, seconds)
        }

        // Marque effect
        LaunchedEffect(key1 = shouldAnimated) {
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(500, 100, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
            )
            scrollState.scrollTo(0)
            shouldAnimated = !shouldAnimated
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            var showMenu by remember { mutableStateOf(false) }
            val sekbarstate = MyEventBus.currentTime.collectAsState()
            var sekbarValue by remember { mutableStateOf(sekbarstate.value) }

            TopAppBar(
                modifier = Modifier.height(50.dp),
                colors = TopAppBarDefaults.mediumTopAppBarColors(Color(cn.getColor(R.color.black_och))),
                title = {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(5.dp)
                                .padding(start = 4.dp),
                            painter = painterResource(id = R.drawable.silver_guitar),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 4.dp),
                            text = "Music Player",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700),
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    showMenu = !showMenu
                                },
                            painter = painterResource(id = R.drawable.three_dots),
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier
                                        .height(50.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(10.dp),
                                        painter = painterResource(id = R.drawable.network),
                                        contentDescription = null
                                    )
                                    Text(text = "Ulashish")
                                }
                            },
                            onClick = {
                                showMenu = false
                                val appMsg: String =
                                    "Hey !, Chack out this app for Share Button :-" +
                                            "https://play.google.com/store/apps/details?id=uz.gita.music_app_jamik"
                                val intent = Intent()
                                intent.action = Intent.ACTION_SEND
                                intent.putExtra(Intent.EXTRA_TEXT, appMsg)
                                intent.type = "test/plain"
                                cn.startActivity(intent)
                            })

                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier
                                        .height(50.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(10.dp),
                                        painter = painterResource(id = R.drawable.telegram),
                                        contentDescription = null
                                    )
                                    Text(text = "Telegram")
                                }
                            },
                            onClick = {
                                showMenu = false
                                val uri = Uri.parse("https://t.me/jamik_gamer")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                cn.startActivity(intent)
                            })

                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier
                                        .height(50.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(10.dp),
                                        painter = painterResource(id = R.drawable.instagram),
                                        contentDescription = null
                                    )
                                    Text(text = "Instagram")
                                }
                            },
                            onClick = {
                                showMenu = false
                                cn.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://instagram.com/_mr.jamik_?igshid=ZDdkNTZiNTM=")
                                    )
                                )
                            })

                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier
                                        .height(50.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(10.dp),
                                        painter = painterResource(id = R.drawable.tik_tok),
                                        contentDescription = null
                                    )
                                    Text(text = "Tik Tok")
                                }
                            },
                            onClick = {
                                showMenu = false
                                cn.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.tiktok.com/@jamik_gamer_?_t=8bhYxtZbfpS&_r=1")
                                    )
                                )

                            })
                    }
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .background(Color.Black)
            ) {

            }

            if (MyEventBus.currentMusicData.collectAsState().value != null) {
                val musData = MyEventBus.currentMusicData.collectAsState().value!!
                val txt =
                    if (musData.title == null) "Unknown" else musData.title.toString()
                Text(
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState, false)
                        .padding(horizontal = 16.dp),
                    text = txt,
                    color = Color.White
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
                    .padding(horizontal = 18.dp)
            ) {
                for (i in 0 until MyEventBus.cursor!!.count) {
                    item {
                        PlayMusicItem(
                            musicData = MyEventBus.cursor!!.getMusicDataByPosition(i),
                            index = i,
                            onLongClick = {
                                MyEventBus.musicPos = i
                                startMusicService(cn, CommandEnum.PLAY)
                            }
                        )

                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = currentTime,
                    fontSize = 18.sp,
                    color = Color(cn.getColor(R.color.silver))
                )

                Slider(
                    value = sekbarstate.value.toFloat(),
                    onValueChange = { newValue ->
                        sekbarValue = newValue.toInt()
                        MyEventBus.currentTime.value = sekbarValue
                    },
                    onValueChangeFinished = {
                        MyEventBus.currentTime.value = sekbarValue
                        startMusicService(cn, CommandEnum.UPDATE_SEEKBAR)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    steps = 1000,
                    valueRange = 0f..musicData.value!!.duration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(cn.getColor(R.color.green)),
                        activeTickColor = Color(cn.getColor(R.color.green)),
                        activeTrackColor = Color(cn.getColor(R.color.green)),
                        inactiveTickColor = Color.Gray,
                        inactiveTrackColor = Color.Transparent
                    )
                )

                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = musicTime,
                    fontSize = 18.sp,
                    color = Color(cn.getColor(R.color.silver))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Image(
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.5f,
                    painter = painterResource(id = R.drawable.play_back_fon),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                val audio: AudioManager = cn.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                    )

                    CircularSeekBar(
                        size = 140.dp,
                        lineWeight = 12.dp,
                        dotRadius = 12,
                        dotColor = Color(cn.getColor(R.color.green1)),
                        value = currentVolume,
                        onChangeListener = {
                            musicScore = false
                            setVolume(it, cn) }
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 30.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        ItemsBtn(
                            pic = R.drawable.prev_icon,
                            size = 40.dp,
                            onClick = {
                                startMusicService(cn, CommandEnum.PREV)
                            musicScore = false
                            })
                        Spacer(modifier = Modifier.width(40.dp))
                        if (MyEventBus.isPlaying.collectAsState().value) {
                            ItemsBtn(
                                pic = R.drawable.pause_icon,
                                size = 50.dp,
                                onClick = {
                                    startMusicService(cn, CommandEnum.PAUSE)
                                musicScore = false
                                })
                        } else {
                            ItemsBtn(
                                pic = R.drawable.play_icon,
                                size = 50.dp,
                                onClick = {
                                    startMusicService(cn, CommandEnum.PLAY)
                                musicScore = false
                                })
                        }
                        Spacer(modifier = Modifier.width(40.dp))

                        ItemsBtn(
                            pic = R.drawable.next_icon,
                            size = 40.dp,
                            onClick = {
                                startMusicService(cn, CommandEnum.NEXT)
                            musicScore = false
                            })
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 25.dp)
                ) {
                    Spacer(modifier = Modifier.weight(0.5f))
//                    if (MyEventBus.isRepeated.collectAsState().value) {
//                        ItemsBtn(
//                            pic = R.drawable.norepeat,
//                            size = 40.dp,
//                            onClick = { MyEventBus.isRepeated.value = false })
//                    } else {
//                        ItemsBtn(
//                            pic = R.drawable.repeat,
//                            size = 40.dp,
//                            onClick = { MyEventBus.isRepeated.value = true })
//                    }

                    ItemsBtn(
                        text = "EQ",
                        size = 40.dp,
                        onClick = {
                            musicScore = false 
                        /* MyEventBus.isRepeated.value = true*/
                        })

                    Spacer(modifier = Modifier.weight(4f))

                    if (MyEventBus.isRepeated.collectAsState().value) {
                        ItemsBtn(
                            pic = R.drawable.norepeat,
                            size = 40.dp,
                            onClick = {
                                MyEventBus.isRepeated.value = false
                            musicScore = false
                            })
                    } else {
                        ItemsBtn(
                            pic = R.drawable.repeat,
                            size = 40.dp,
                            onClick = {
                                MyEventBus.isRepeated.value = true
                            musicScore = false
                            })
                    }
                    Spacer(modifier = Modifier.weight(3f))
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(0.5f))
                        if (musicScore) {
                            ItemsBtn(
                                pic = R.drawable.speed,
                                size = 40.dp,
                                onClick = { musicScore = false })
                        } else {
                            ItemsBtn(
                                pic = R.drawable.nospeed,
                                size = 40.dp,
                                onClick = { musicScore = true })
                        }

                        if (musicScore) {

                            Spacer(modifier = Modifier.weight(0.2f))

                            ItemsBtn(text = "0.5x", size = 30.dp, onClick = {
                                MyEventBus.speed.value = SpeedEnum.Sekin
                                startMusicService(cn, CommandEnum.Speed)
                                musicScore = false
                            })

                            Spacer(modifier = Modifier.weight(0.2f))

                            ItemsBtn(text = "1x", size = 30.dp, onClick = {
                                MyEventBus.speed.value = SpeedEnum.Ortacha
                                startMusicService(cn, CommandEnum.Speed)
                                musicScore = false
                            })

                            Spacer(modifier = Modifier.weight(0.2f))

                            ItemsBtn(text = "1.5x", size = 30.dp, onClick = {
                                MyEventBus.speed.value = SpeedEnum.Tez
                                startMusicService(cn, CommandEnum.Speed)
                                musicScore = false
                            })

                            Spacer(modifier = Modifier.weight(0.2f))
                            ItemsBtn(text = "2x", size = 30.dp, onClick = {
                                MyEventBus.speed.value = SpeedEnum.JudaTez
                                startMusicService(cn, CommandEnum.Speed)
                                musicScore = false
                            })

                            Spacer(modifier = Modifier.weight(0.2f))
                        } else
                            Spacer(modifier = Modifier.weight(4f))

                        if (MyEventBus.isRandom.collectAsState().value) {
                            ItemsBtn(
                                pic = R.drawable.norandom,
                                size = 40.dp,
                                onClick = {
                                    MyEventBus.isRandom.value = false
                                    musicScore = false })
                        } else {
                            ItemsBtn(
                                pic = R.drawable.random,
                                size = 40.dp,
                                onClick = {
                                    MyEventBus.isRandom.value = true
                                musicScore = false
                                })
                        }
                        Spacer(modifier = Modifier.weight(3f))
                    }

                }

            }
        }
    }

    @Composable
    private fun ItemsBtn(
        onClick: (() -> Unit)? = null,
        @DrawableRes
        pic: Int = R.drawable.play_icon,
        size: Dp = 20.dp,
    ) {
        val cn = LocalContext.current

        Box(
            modifier = Modifier
                .size(size + 5.dp)
                .clickable { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            val pr = 0f
            CircularSeekbarView(
                modifier = Modifier.clickable { onClick?.invoke() },
                value = pr,
                onChange = {},
                activeColor = Color(cn.getColor(R.color.green1)),
                dotRadius = 0.dp,
                lineWeight = 2.dp,
                inactiveColor = Color(cn.getColor(R.color.green1))
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .clickable { onClick?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .clickable { onClick?.invoke() },
                    painter = painterResource(id = pic),
                    contentDescription = null
                )
            }
        }

    }

    @Composable
    private fun ItemsBtn(
        onClick: (() -> Unit)? = null,
        text: String = "",
        size: Dp = 20.dp,
    ) {
        val cn = LocalContext.current

        Box(
            modifier = Modifier
                .size(size + 5.dp)
                .clickable { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            val pr = 0f
            CircularSeekbarView(
                modifier = Modifier.clickable { onClick?.invoke() },
                value = pr,
                onChange = {},
                activeColor = Color(cn.getColor(R.color.green1)),
                dotRadius = 0.dp,
                lineWeight = 2.dp,
                inactiveColor = Color(cn.getColor(R.color.green1))
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .clickable { onClick?.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable { onClick?.invoke() },
                    text = text,
                    fontSize = 8.sp,
                    color = Color(cn.getColor(R.color.green)),
                    textAlign = TextAlign.Center
                )
            }
        }

    }

    @Preview
    @Composable
    private fun PlayContentPrev() {

        MaterialTheme() {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                PlayContent()
            }
        }

    }

}