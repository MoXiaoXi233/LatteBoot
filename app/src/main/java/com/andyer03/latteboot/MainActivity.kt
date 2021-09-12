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
import androidx.core.content.ContextCompat
import com.andyer03.latteboot.*
import com.andyer03.latteboot.commands.BootFile
import com.andyer03.latteboot.commands.LatteSwitchCom
import com.andyer03.latteboot.commands.Root
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
            changeTitle()
            init()
        }
    }

    @SuppressLint("SdCardPath")
    private fun init() = with(binding) {
        val tempFile = "/sdcard/.latteboot"
        val winBoot = File(tempFile).exists()

        if (Root().check()) {
            if (!winBoot) {
                System("mountefi").boot()
                Runtime.getRuntime()
                    .exec("su -c cp /mnt/cifs/efi/EFI/BOOT/bootx64.efi.win /sdcard/.latteboot")
            }
        }

        val imageIdList = listOf (
            R.drawable.ic_restart,
            R.drawable.ic_shield,
            R.drawable.ic_power,
            R.drawable.ic_recovery,
            R.drawable.ic_bootloader,
            R.drawable.ic_power,
            R.drawable.ic_android,
            R.drawable.ic_windows
        )

        val safemodeTitle = getString(R.string.reboot_safe_mode_title)
        val screenoffTitle = getString(R.string.reboot_screen_off_title)
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

        val safemode = BootOptions(imageIdList[1], safemodeTitle)
        val screenoff = BootOptions(imageIdList[2], screenoffTitle)
        val recovery = BootOptions(imageIdList[3], recoveryTitle)
        val bootloader = BootOptions(imageIdList[4], bootloaderTitle)
        val dnx = BootOptions(imageIdList[4], dnxTitle)
        val shutdown = BootOptions(imageIdList[5], shutdownTitle)

        adapter.addBootOptions(screenoff)
        adapter.addBootOptions(shutdown)
        adapter.addBootOptions(recovery)
        adapter.addBootOptions(bootloader)
        adapter.addBootOptions(dnx)
        adapter.addBootOptions(safemode)

        if (Root().check()) {
            if (winBoot) {
                val window = this@MainActivity.window
                val activity = this@MainActivity
                if (BootFile().check()) {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)

                    val rebootTitle = getString(R.string.reboot_device_title)
                    val windows = getString(R.string.reboot_win_title)
                    val reboot = BootOptions(imageIdList[7], "$rebootTitle\n$windows")
                    adapter.addBootOptions(reboot)

                    val androidTitle = getString(R.string.reboot_and_title)
                    val android = BootOptions(imageIdList[6], androidTitle)
                    adapter.addBootOptions(android)
                } else if (!BootFile().check()) {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.green)

                    val rebootTitle = getString(R.string.reboot_device_title)
                    val android = getString(R.string.reboot_and_title)
                    val reboot = BootOptions(imageIdList[6], "$rebootTitle\n$android")
                    adapter.addBootOptions(reboot)

                    val windowsTitle = getString(R.string.reboot_win_title)
                    val windows = BootOptions(imageIdList[7], windowsTitle)
                    adapter.addBootOptions(windows)
                } else {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.orange)

                    val rebootTitle = getString(R.string.reboot_device_title)
                    val reboot = BootOptions(imageIdList[0], rebootTitle)
                    adapter.addBootOptions(reboot)
                }
            } else {
                val rebootTitle = getString(R.string.reboot_device_title)
                val reboot = BootOptions(imageIdList[0], rebootTitle)
                adapter.addBootOptions(reboot)
            }
        } else {
            val rebootTitle = getString(R.string.reboot_device_title)
            val reboot = BootOptions(imageIdList[0], rebootTitle)
            adapter.addBootOptions(reboot)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.swap -> {
                when {
                    Root().check() -> {
                        LatteSwitchCom().execute()
                        when {
                            !BootFile().check() -> {
                                Toast.makeText(this, R.string.next_boot_android, Toast.LENGTH_SHORT).show()
                            }
                            BootFile().check() -> {
                                Toast.makeText(this, R.string.next_boot_windows, Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                    }
                }
                recreate()
            }
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

    private fun changeTitle() {
        when {
            Root().check() && BootFile().check() -> {
                this.title = getString(R.string.next_boot_windows)

            }
            Root().check() && !BootFile().check() -> {
                this.title = getString(R.string.next_boot_android)
            }
        }
    }

}