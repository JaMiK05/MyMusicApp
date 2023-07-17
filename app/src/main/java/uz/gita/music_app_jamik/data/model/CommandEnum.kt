package uz.gita.music_app_jamik.data.model

enum class CommandEnum(val amount: Int) {
    MANAGE(6), PREV(1),
    NEXT(2), PLAY(3),
    PAUSE(4), CLOSE(5),
    CONTINUE(7), UPDATE_SEEKBAR(8),
    IS_REPEATED(8), Speed(9)
}