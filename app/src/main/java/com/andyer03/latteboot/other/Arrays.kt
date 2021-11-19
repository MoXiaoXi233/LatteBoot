package com.andyer03.latteboot.other

import android.content.res.Resources
import com.andyer03.latteboot.BootOptions
import com.andyer03.latteboot.R

class Arrays {

    // Define icons
    private val optionsImage = listOf (
        R.drawable.ic_reboot, // 0 Reboot
        R.drawable.ic_safe_mode, // 1 Safemode
        R.drawable.ic_power, // 2 Power
        R.drawable.ic_recovery, // 3 Recovery
        R.drawable.ic_bootloader, // 4 Bootloader
        R.drawable.ic_android, // 5 Android
        R.drawable.ic_windows // 6 Windows
    )

    // Define strings
    val optionsTitles = arrayOf (
        Resources.getSystem().getString(R.string.reboot_screen_off_title), // 0 Screen off
        Resources.getSystem().getString(R.string.reboot_shutdown_title), // 1 Shutdown
        Resources.getSystem().getString(R.string.reboot_recovery_title), // 2 Recovery
        Resources.getSystem().getString(R.string.reboot_bootloader_title), // 3 Bootloader
        Resources.getSystem().getString(R.string.reboot_dnx_title), // 4 DNX
        Resources.getSystem().getString(R.string.reboot_safe_mode_title), // 5 Safe mode
        Resources.getSystem().getString(R.string.reboot_device_title), // 6 Reboot
        Resources.getSystem().getString(R.string.reboot_android_title), // 7 Android
        Resources.getSystem().getString(R.string.reboot_windows_title), // 8 Windows
    )

    // Define current bootloader
    private val currentBootloader = arrayOf (
        "", // Nothing
        Resources.getSystem().getString(R.string.boot_item_current) // Current bootloader
    )

    // Define icons + strings together
    val bootOptions = arrayOf (
        BootOptions(optionsImage[2], optionsTitles[0], currentBootloader[0]), // 0 Screen off
        BootOptions(optionsImage[2], optionsTitles[1], currentBootloader[0]), // 1 Shutdown
        BootOptions(optionsImage[3], optionsTitles[2], currentBootloader[0]), // 2 Recovery
        BootOptions(optionsImage[4], optionsTitles[3], currentBootloader[0]), // 3 Bootloader
        BootOptions(optionsImage[4], optionsTitles[4], currentBootloader[0]), // 4 DNX
        BootOptions(optionsImage[1], optionsTitles[5], currentBootloader[0]), // 5 Safemode
        BootOptions(optionsImage[0], optionsTitles[6], currentBootloader[0]), // 6 Simple reboot
        BootOptions(optionsImage[5], optionsTitles[7], currentBootloader[0]), // 7 Android
        BootOptions(optionsImage[6], optionsTitles[8], currentBootloader[0]), // 8 Windows
        BootOptions(optionsImage[5], optionsTitles[7], currentBootloader[1]), // 9 Android Current
        BootOptions(optionsImage[6], optionsTitles[8], currentBootloader[1]), // 10 Windows Current
    )
}