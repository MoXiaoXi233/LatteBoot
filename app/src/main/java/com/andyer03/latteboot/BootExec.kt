package com.andyer03.latteboot

import com.andyer03.latteboot.Com as C

class BootExec {
    fun lsData() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().lsData))
    }
    fun android() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().reboot))
    }
    fun mountefi() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().remount)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().cifs)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().efi)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().umount)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().mount)).waitFor()
    }
    fun windows() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().miui)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().win)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().reboot))
    }
    fun recovery() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().recovery))
    }
    fun bootloader() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().bootloader))
    }
    fun dnx() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().dnx))
    }
    fun shutdown() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().shutdown))
    }
    fun safemode() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().safemode))
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().mountSafeModeFile))
        android()
    }
    fun screenoff() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().screenoff))
    }
}