package com.andyer03.latteboot

import com.andyer03.latteboot.Com as C

class BootExec {
    fun android() {
        Runtime.getRuntime().exec(C().reboot)
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
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().recovery))
    }
    fun bootloader() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().bootloader))
    }
    fun dnx() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().dnx))
    }
    fun shutdown() {
        Runtime.getRuntime().exec(arrayOf(C().reboot, C().shutdown))
    }
    fun safemode() {
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().safemode)).waitFor()
        Runtime.getRuntime().exec(arrayOf(C().su, C().c, C().reboot))
    }
    fun screenoff() {
        Runtime.getRuntime().exec(arrayOf(C().screenoff))
    }
}