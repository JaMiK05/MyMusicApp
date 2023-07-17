package uz.gita.music_app_jamik.data.model

/**
 *   Created by Jamik on 7/16/2023 ot 2:09 PM
 **/
enum class SpeedEnum(val speed: Long) {
    SLOW(500),
    NORMAL(1000),
    FAST(1500),
    VERY_FAST(2000)
}