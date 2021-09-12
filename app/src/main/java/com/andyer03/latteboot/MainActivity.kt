package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import java.io.File
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.andyer03.latteboot.*
import com.andyer03.latteboot.commands.BootFile
import com.andyer03.latteboot.commands.LatteSwitchCom
import com.andyer03.latteboot.commands.Root
import com.andyer03.latteboot.commands.System
import com.andyer03.latteboot.other.Device
import com.andyer03.latteboot.shortcuts.*

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
            loadPreferences()
            init()
        }
    }

    override fun onResume() {
        super.onResume()
        regSettingsChangeListener()
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
                if (BootFile().check()) {
                    val rebootTitle = getString(R.string.reboot_device_title)
                    val windows = getString(R.string.reboot_win_title)
                    val reboot = BootOptions(imageIdList[7], "$rebootTitle\n$windows")
                    adapter.addBootOptions(reboot)

                    val androidTitle = getString(R.string.reboot_and_title)
                    val android = BootOptions(imageIdList[6], androidTitle)
                    adapter.addBootOptions(android)
                } else if (!BootFile().check()) {
                    val rebootTitle = getString(R.string.reboot_device_title)
                    val android = getString(R.string.reboot_and_title)
                    val reboot = BootOptions(imageIdList[6], "$rebootTitle\n$android")
                    adapter.addBootOptions(reboot)

                    val windowsTitle = getString(R.string.reboot_win_title)
                    val windows = BootOptions(imageIdList[7], windowsTitle)
                    adapter.addBootOptions(windows)
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

    open fun regSettingsChangeListener() {
        val window = this@MainActivity.window
        val activity = this@MainActivity
        val settingsPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sharedPreferences = getSharedPreferences("window", Context.MODE_PRIVATE)

        settingsPreferences.registerOnSharedPreferenceChangeListener { _, _ ->
            val p = packageManager
            val rebootDeviceComponentName = ComponentName(applicationContext, RebootDevice::class.java)
            val screenOffComponentName = ComponentName(applicationContext, ScreenOff::class.java)
            val rebootRecoveryComponentName = ComponentName(applicationContext, RebootRecovery::class.java)
            val rebootFastbootComponentName = ComponentName(applicationContext, RebootFastboot::class.java)
            val rebootDNXComponentName = ComponentName(applicationContext, RebootDNX::class.java)
            val powerDownComponentName = ComponentName(applicationContext, ShutDown::class.java)
            val rebootSafemodeComponentName = ComponentName(applicationContext, RebootSafeMode::class.java)
            val rebootAndroidComponentName = ComponentName(applicationContext, RebootAndroid::class.java)
            val rebootWindowsComponentName = ComponentName(applicationContext, RebootWindows::class.java)

            // Changing title depending on bootloader
            when (settingsPreferences.getBoolean("window_title", false)) {
                true -> {
                    changeTitle()
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("window_title_pref", true)
                    editor.apply()
                }
                false -> {
                    this.title = getString(R.string.app_name)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("window_title_pref", false)
                    editor.apply()
                }
            }

            // Changing status bar color depending on bootloader
            when {
                BootFile().check() && settingsPreferences.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar_pref", true)
                    editor.apply()
                }
                !BootFile().check() && settingsPreferences.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar_pref", true)
                    editor.apply()
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar_pref", false)
                    editor.apply()
                }
            }

            // Show or hide shortcuts from app drawer
            when (settingsPreferences.getBoolean("reboot", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootDeviceComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootDeviceComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("screen_off", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        screenOffComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        screenOffComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("recovery", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootRecoveryComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootRecoveryComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("fastboot", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootFastbootComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootFastbootComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("dnx", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootDNXComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootDNXComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("power_down", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        powerDownComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        powerDownComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("safe_mode", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootSafemodeComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootSafemodeComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("android", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootAndroidComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootAndroidComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("windows", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        rebootWindowsComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        rebootWindowsComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
        }
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

    private fun loadPreferences() {
        val window = this@MainActivity.window
        val activity = this@MainActivity
        val sp = getSharedPreferences("window", Context.MODE_PRIVATE)

        // Changing title depending on bootloader
        when (sp.getBoolean("window_title_pref", false)) {
            true -> {
                changeTitle()
            }
            false -> {
                this.title = getString(R.string.app_name)
            }
        }

        // Changing status bar color depending on bootloader
        when {
            BootFile().check() && sp.getBoolean("status_bar_pref", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
            }
            !BootFile().check() && sp.getBoolean("status_bar_pref", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
            }
            else -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
            }
        }
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