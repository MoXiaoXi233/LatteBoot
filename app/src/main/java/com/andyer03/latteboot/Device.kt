package com.andyer03.latteboot

class Device {
    private val brand = "Xiaomi"
    private val model = "MI PAD 2"
    private val board = "latte"

    fun check(): Boolean {
        return android.os.Build.BRAND == Device().brand && android.os.Build.MODEL == Device().model && android.os.Build.BOARD == Device().board
    }
}