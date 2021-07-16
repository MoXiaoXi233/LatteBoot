package com.andyer03.latteboot

class System (private val OS: String) : MainActivity() {

    fun boot() {
        when (OS) {
            "lsData" -> {
                BootExec().lsData()
            }
            "and" -> {
                BootExec().android()
            }
            "mountefi" -> {
                BootExec().mountefi()
            }
            "win" -> {
                BootExec().windows()
            }
            "rec" -> {
                BootExec().recovery()
            }
            "fbt" -> {
                BootExec().bootloader()
            }
            "dnx" -> {
                BootExec().dnx()
            }
            "pwd" -> {
                BootExec().shutdown()
            }
            "sfm" -> {
                BootExec().safemode()
            }
            "scn" -> {
                BootExec().screenoff()
            }
        }
    }
}