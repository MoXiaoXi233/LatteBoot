package com.andyer03.latteboot.commands

import com.andyer03.latteboot.MainActivity

@ExperimentalStdlibApi
class System (private val OS: String) : MainActivity() {

    fun boot() {
        when (OS) {
            "reboot" -> {
                BootExec().reboot()
            }
            "scn" -> {
                BootExec().screenOff()
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
            "mountEFI" -> {
                BootExec().mountEFI()
            }
            "sfm" -> {
                BootExec().safeMode()
            }
            "win" -> {
                BootExec().windows()
            }
            "and" -> {
                BootExec().android()
            }
        }
    }
}