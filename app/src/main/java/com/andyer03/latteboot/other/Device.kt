package com.andyer03.latteboot.other

class Device {
    private val brand = "Xiaomi"
    private val model = "MI PAD 2"
    private val board = "latte"
    val androidHash = "566aae30"
    val windowsHash = "50a25ce6"

    fun check(): Boolean {
        return android.os.Build.BRAND == Device().brand && android.os.Build.MODEL == Device().model && android.os.Build.BOARD == Device().board
    }
}