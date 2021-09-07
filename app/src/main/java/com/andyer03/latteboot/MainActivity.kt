package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import java.io.File
import android.content.pm.PackageManager
import android.content.ComponentName

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = BootAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Device().check()) {
            Toast.makeText(this, R.string.mi_pad_2_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            Runtime.getRuntime().exec(arrayOf(Com().su, Com().c)).waitFor()
            init()
        }
    }

    @SuppressLint("SdCardPath")
    private fun init() = with(binding) {
        val tempFile = "/sdcard/.latteboot"
        val latteboot = File(tempFile).exists()
        if (!latteboot) {
            System("mountefi").boot()
            Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /sdcard/.latteboot")
        }

        // Show or hide Windows icon from app drawer
        val p = packageManager
        val componentName = ComponentName(applicationContext, RebootWindows::class.java)
        if (latteboot) {
            p.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } else {
            p.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        val bin = File("/system/bin/su")
        val xbin = File("/system/xbin/su")

        val imageIdList = listOf (
            R.drawable.ic_restart,
            R.drawable.ic_shield,
            R.drawable.ic_shutdown,
            R.drawable.ic_recovery,
            R.drawable.ic_bootloader,
            R.drawable.ic_dnx,
            R.drawable.ic_shutdown,
            R.drawable.ic_windows
        )

        val rebootTitle = getString(R.string.reboot_device_title)
        val safemodeTitle = getString(R.string.reboot_safemode_title)
        val screenoffTitle = getString(R.string.reboot_screenoff_title)
        val recoveryTitle = getString(R.string.reboot_recovery_title)
        val bootloaderTitle = getString(R.string.reboot_bootloader_title)
        val dnxTitle = getString(R.string.reboot_dnx_title)
        val shutdownTitle = getString(R.string.reboot_shutdown_title)

        val orientation = resources.configuration.orientation
        val spanCount: Int = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        rcView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)
        rcView.adapter = adapter

        val reboot = BootOptions(imageIdList[0], rebootTitle)
        val safemode = BootOptions(imageIdList[1], safemodeTitle)
        val screenoff = BootOptions(imageIdList[2], screenoffTitle)
        val recovery = BootOptions(imageIdList[3], recoveryTitle)
        val bootloader = BootOptions(imageIdList[4], bootloaderTitle)
        val dnx = BootOptions(imageIdList[5], dnxTitle)
        val shutdown = BootOptions(imageIdList[6], shutdownTitle)

        adapter.addBootOptions(reboot)
        adapter.addBootOptions(screenoff)
        adapter.addBootOptions(recovery)
        adapter.addBootOptions(bootloader)
        adapter.addBootOptions(dnx)
        adapter.addBootOptions(shutdown)

        if (bin.exists() || xbin.exists()) {
            adapter.addBootOptions(safemode)
        }

        if ((bin.exists() || xbin.exists()) && latteboot) {
            val windowsTitle = getString(R.string.reboot_win_title)
            val windows = BootOptions(imageIdList[7], windowsTitle)
            adapter.addBootOptions(windows)
        }
    }
}