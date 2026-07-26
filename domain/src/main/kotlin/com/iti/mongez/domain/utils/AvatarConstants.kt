package com.iti.mongez.domain.utils

object AvatarConstants {
    val AVATARS = listOf(
        "https://cdn-icons-png.flaticon.com/128/4140/4140037.png",
        "https://cdn-icons-png.flaticon.com/128/4140/4140047.png",
        "https://cdn-icons-png.flaticon.com/128/4333/4333609.png",
        "https://cdn-icons-png.flaticon.com/128/4140/4140039.png",
        "https://cdn-icons-png.flaticon.com/128/4140/4140060.png",
        "https://cdn-icons-png.flaticon.com/128/4140/4140040.png",
        "https://cdn-icons-png.flaticon.com/128/4139/4139981.png",
        "https://cdn-icons-png.flaticon.com/128/4139/4139951.png",
        "https://cdn-icons-png.flaticon.com/128/4526/4526437.png",
        "https://cdn-icons-png.flaticon.com/128/4140/4140062.png"
    )

    fun getRandomAvatar(): String = AVATARS.random()
}
