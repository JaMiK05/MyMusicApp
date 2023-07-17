package uz.gita.music_app_jamik.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import uz.gita.music_app_jamik.*
import uz.gita.music_app_jamik.data.model.*
import uz.gita.music_app_jamik.util.*
import java.util.Random


/**
 *   Created by Jamik on 7/15/2023 ot 3:35 PM
 **/
class MusicService : Service() {

    companion object {
        const val CHANNEL_ID = "My music player"
        const val CHANNEL_NAME = "Music player"
    }

    private var _musicPlayer: MediaPlayer? = null
    private val musicPlayer get() = _musicPlayer!!

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private var job: Job? = null


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        Log.d("JJJ", "createChannel")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val mCHannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mCHannel)
        }
    }

    private fun createNotification(music: MusicData) {

        val myIntent = Intent(this, MainActivity::class.java).apply {
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val myPendingIntent =
            PendingIntent.getActivity(this, 1, myIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.remote_disk)
            .setCustomContentView(createRemoteView(music))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(myPendingIntent)
            .setColorized(true)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
        startForeground(1, notificationBuilder.build())
    }

    private fun createRemoteView(music: MusicData): RemoteViews {
        val view = RemoteViews(this.packageName, R.layout.remote_views)
        view.setTextViewText(R.id.musicname, music.title)
        view.setTextViewText(R.id.musicowner, music.artist)
        if (music.albumArt != null)
            view.setImageViewBitmap(R.id.musicphoto, music.albumArt)
        else
            view.setImageViewResource(R.id.musicphoto, R.drawable.remote_disk)

        if (_musicPlayer != null && !musicPlayer.isPlaying) {
            view.setImageViewResource(R.id.playOrPause, R.drawable.remote_play)
        } else {
            view.setImageViewResource(R.id.playOrPause, R.drawable.remote_pause)
        }

        view.setOnClickPendingIntent(R.id.musicprev, createPendingIntent(CommandEnum.PREV))
        view.setOnClickPendingIntent(R.id.playOrPause, createPendingIntent(CommandEnum.MANAGE))
        view.setOnClickPendingIntent(R.id.musicnext, createPendingIntent(CommandEnum.NEXT))
        view.setOnClickPendingIntent(R.id.musiccancel, createPendingIntent(CommandEnum.CLOSE))
        return view
    }

    private fun createPendingIntent(command: CommandEnum): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("Command", command)
        return PendingIntent.getService(
            this,
            command.amount,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if ((MyEventBus.cursor == null || MyEventBus.musicPos == -1))
            return START_NOT_STICKY

        val command = intent?.extras?.getSerializable("COMMAND") as CommandEnum
        doneCommand(command)
        createNotification(MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.musicPos))

        return START_NOT_STICKY
    }

    private fun doneCommand(command: CommandEnum) {
        when (command) {
            CommandEnum.MANAGE -> {
                if (musicPlayer.isPlaying) doneCommand(CommandEnum.PAUSE)
                else doneCommand(CommandEnum.CONTINUE)
            }

            CommandEnum.CONTINUE -> {
                job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                musicPlayer.seekTo(MyEventBus.currentTime.value)
                scope.launch { MyEventBus.isPlaying.emit(true) }
                musicPlayer.start()
            }

            CommandEnum.UPDATE_SEEKBAR -> {
                if (_musicPlayer == null) return
                if (musicPlayer.isPlaying) {
                    job?.cancel()
                    musicPlayer.seekTo(MyEventBus.currentTime.value)
                    job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                } else {
                    musicPlayer.seekTo(MyEventBus.currentTime.value)
                    job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                    job?.cancel()
                }
            }

            CommandEnum.PREV -> {
                if (!MyEventBus.isRepeated.value) {
                    if (MyEventBus.currentTime.value <= 5000) {
                        if (MyEventBus.isRandom.value) {
                            randomMusic()
                        } else {
                            if (MyEventBus.musicPos == 0) {
                                MyEventBus.musicPos = MyEventBus.cursor!!.count - 1
                            } else {
                                MyEventBus.musicPos -= 1
                            }
                        }
                    }
                }
                MyEventBus.playMusicPos = -1
                doneCommand(CommandEnum.PLAY)
            }

            CommandEnum.NEXT -> {
                if (!MyEventBus.isRepeated.value) {
                    if (MyEventBus.isRandom.value) {
                        randomMusic()
                    } else {
                        if (MyEventBus.musicPos == MyEventBus.cursor!!.count - 1) {
                            MyEventBus.musicPos = 0
                        } else {
                            MyEventBus.musicPos += 1
                        }
                    }
                }
                MyEventBus.playMusicPos = -1
                doneCommand(CommandEnum.PLAY)
            }

            CommandEnum.PLAY -> {

                if (MyEventBus.playMusicPos == MyEventBus.musicPos) {
                    job?.cancel()
                    job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                    scope.launch { MyEventBus.isPlaying.emit(true) }
                    musicPlayer.start()
                } else {
                    val data = MyEventBus.cursor!!.getMusicDataByPosition(MyEventBus.musicPos)
                    scope.launch { MyEventBus.currentMusicData.emit(data) }
                    MyEventBus.currentTime.value = 0
                    MyEventBus.musicTime.value = data.duration.toInt()
                    _musicPlayer?.stop()
                    _musicPlayer = MediaPlayer.create(this, Uri.parse(data.data))
                    try {
                        musicPlayer.setOnCompletionListener {
                            if (MyEventBus.isRepeated.value) {
                                MyEventBus.playMusicPos = -1
                                doneCommand(CommandEnum.PLAY)
                            } else {
                                doneCommand(CommandEnum.NEXT)
                            }
                        }
                        musicPlayer.start()
                        MyEventBus.playMusicPos = MyEventBus.musicPos
                        job?.cancel()
                        job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }
                            .launchIn(scope)
                        scope.launch { MyEventBus.isPlaying.emit(true) }
                    } catch (e: Exception) {
                        scope.launch {
                            MyEventBus.message.value = e.message.toString()
                            delay(200)
                            MyEventBus.message.value = ""
                        }
                    }
                }
            }

            CommandEnum.Speed -> {
                var speed = 0f
                when (MyEventBus.speed.value) {
                    SpeedEnum.Sekin -> {
                        speed = 0.5f
                    }

                    SpeedEnum.Ortacha -> {
                        speed = 1f
                    }

                    SpeedEnum.Tez -> {
                        speed = 1.5f
                    }

                    SpeedEnum.JudaTez -> {
                        speed = 2f
                    }
                }

                if (_musicPlayer == null) return
                if (MyEventBus.isPlaying.value) {
                    job?.cancel()
                    musicPlayer.pause()
                    val playbackParams = PlaybackParams()
                    playbackParams.speed = speed
                    musicPlayer.playbackParams = playbackParams
                    musicPlayer.start()
                    job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                } else {
                    val playbackParams = PlaybackParams()
                    playbackParams.speed = speed
                    musicPlayer.playbackParams = playbackParams
                    musicPlayer.pause()
                    job = moveProgress().onEach { MyEventBus.currentTime.emit(it) }.launchIn(scope)
                    job?.cancel()
                }
            }

            CommandEnum.PAUSE -> {
                musicPlayer.pause()
                musicPlayer.seekTo(MyEventBus.currentTime.value)
                job?.cancel()
                scope.launch { MyEventBus.isPlaying.emit(false) }
            }

            CommandEnum.CLOSE -> {
                musicPlayer.pause()
                job?.cancel()
                scope.launch { MyEventBus.isPlaying.emit(false) }
                //stopSelf()
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            }

            CommandEnum.IS_REPEATED -> {
                MyEventBus.isRepeated.value = !MyEventBus.isRepeated.value
                scope.launch {
                    MyEventBus.isRepeated.emit(!MyEventBus.isRepeated.value)
                }
            }

        }
    }

    private fun randomMusic() {
        val random = Random()
        val randomNumber = random.nextInt(MyEventBus.cursor!!.count) // 0 dan 99 gacha tasodifiy son
        if (randomNumber == MyEventBus.musicPos && MyEventBus.cursor!!.count > 10) {
            randomMusic()
        }
        MyEventBus.musicPos = randomNumber
    }

    private fun moveProgress(): Flow<Int> = flow {
        for (i in MyEventBus.currentTime.value until MyEventBus.musicTime.value step 1000) {
            emit(i)
            delay(MyEventBus.speed.value.speed)
            Log.d("speed", MyEventBus.speed.value.speed.toString())
        }
    }

}