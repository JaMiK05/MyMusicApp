package uz.gita.music_app_jamik.data.model

/**
 *   Created by Jamik on 7/16/2023 ot 2:09 PM
 **/
enum class SpeedEnum(val speed: Long, val emitInt: Int) {
    Sekin(2000,500), Ortacha(1000,1000),
    Tez(750,1500), JudaTez(500,2000)
}