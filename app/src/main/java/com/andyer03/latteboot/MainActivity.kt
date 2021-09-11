package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import java.io.File
import android.content.DialogInterface
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.andyer03.latteboot.*
import com.andyer03.latteboot.commands.BootFile
import com.andyer03.latteboot.commands.System
import com.andyer03.latteboot.other.Device

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
            if (!BootFile().check()) {
                this.title = getString(R.string.next_boot_android)
            } else if (BootFile().check()) {
                this.title = getString(R.string.next_boot_windows)
            } else {
                return
            }
            init()
        }
    }

    @SuppressLint("SdCardPath")
    private fun init() = with(binding) {
        val tempFile = "/sdcard/.latteboot"
        val winBoot = File(tempFile).exists()
        if (!winBoot) {
            System("mountefi").boot()
            Runtime.getRuntime().exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /sdcard/.latteboot")
        }

        val imageIdList = listOf (
            R.drawable.ic_restart,
            R.drawable.ic_shield,
            R.drawable.ic_power,
            R.drawable.ic_recovery,
            R.drawable.ic_bootloader,
            R.drawable.ic_power,
            R.drawable.ic_windows,
            R.drawable.ic_android
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
        val dnx = BootOptions(imageIdList[4], dnxTitle)
        val shutdown = BootOptions(imageIdList[5], shutdownTitle)

        adapter.addBootOptions(reboot)
        adapter.addBootOptions(screenoff)
        adapter.addBootOptions(recovery)
        adapter.addBootOptions(bootloader)
        adapter.addBootOptions(dnx)
        adapter.addBootOptions(shutdown)

        adapter.addBootOptions(safemode)

        if (winBoot) {
            val androidTitle = getString(R.string.reboot_win_title)
            val android = BootOptions(imageIdList[7], androidTitle)
            adapter.addBootOptions(android)

            val windowsTitle = getString(R.string.reboot_win_title)
            val windows = BootOptions(imageIdList[6], windowsTitle)
            adapter.addBootOptions(windows)

            val delayedBootTitle = getString(R.string.reboot_windows_delayed)
            val delayedBoot = BootOptions(imageIdList[6], delayedBootTitle)
            adapter.addBootOptions(delayedBoot)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.about -> {
                aboutDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun aboutDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " " + getString(R.string.about_title))
        builder.setPositiveButton(R.string.exit_button) { _: DialogInterface?, _: Int ->
            DialogInterface.BUTTON_POSITIVE
        }
        builder.show()
    }

}