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
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
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
            // Loading preferences
            loadPreferences()

            // Load the main code
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

        val orientation = resources.configuration.orientation
        val spanCount: Int = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        rcView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)
        rcView.adapter = adapter

        // Define icons
        val optionsImage = listOf (
            R.drawable.ic_reboot, // Reboot
            R.drawable.ic_safe_mode, // Safe mode
            R.drawable.ic_power, // Power
            R.drawable.ic_recovery, // Recovery
            R.drawable.ic_bootloader, // Bootloader
            R.drawable.ic_android, // Android
            R.drawable.ic_windows // Windows
        )

        // Define strings
        val optionsTitles = arrayOf (
            getString(R.string.reboot_screen_off_title), // Screen off
            getString(R.string.reboot_shutdown_title), // Shutdown
            getString(R.string.reboot_recovery_title), // Recovery
            getString(R.string.reboot_bootloader_title), // Bootloader
            getString(R.string.reboot_dnx_title), // DNX
            getString(R.string.reboot_safe_mode_title), // Safe mode
            getString(R.string.reboot_device_title), // Reboot
            getString(R.string.reboot_win_title), // Windows
            getString(R.string.reboot_and_title) // Android
        )

        // Define icons + strings together
        val bootOptions = arrayOf (
            BootOptions(optionsImage[2], optionsTitles[0]), // 0 Screen off
            BootOptions(optionsImage[2], optionsTitles[1]), // 1 Shutdown
            BootOptions(optionsImage[3], optionsTitles[2]), // 2 Recovery
            BootOptions(optionsImage[4], optionsTitles[3]), // 3 Bootloader
            BootOptions(optionsImage[4], optionsTitles[4]), // 4 DNX
            BootOptions(optionsImage[1], optionsTitles[5]), // 5 Safe mode
            BootOptions(optionsImage[0], optionsTitles[6]), // 6 Simple reboot
            BootOptions(optionsImage[6], optionsTitles[7]), // 7 Windows
            BootOptions(optionsImage[5], optionsTitles[8]), // 8 Android
            BootOptions(optionsImage[6], optionsTitles[6]+"\n"+optionsTitles[7]), // 9 Reboot Windows
            BootOptions(optionsImage[5], optionsTitles[6]+"\n"+optionsTitles[8]) // 10 Reboot Android
        )

        // Filling adapter
        adapter.addBootOptions(bootOptions[0]) // Screen off
        adapter.addBootOptions(bootOptions[1]) // Shutdown
        adapter.addBootOptions(bootOptions[2]) // Recovery
        adapter.addBootOptions(bootOptions[3]) // Bootloader
        adapter.addBootOptions(bootOptions[4]) // DNX
        adapter.addBootOptions(bootOptions[5]) // Safe mode

        when {
            Root().check() -> {
                when {
                    winBoot -> {
                        if (BootFile().check()) {
                            adapter.addBootOptions(bootOptions[9]) // Reboot Windows
                            adapter.addBootOptions(bootOptions[8]) // Android
                        } else if (!BootFile().check()) {
                            adapter.addBootOptions(bootOptions[10]) // Reboot Android
                            adapter.addBootOptions(bootOptions[7]) // Windows
                        } else {
                            adapter.addBootOptions(bootOptions[6]) // Simple reboot
                        }
                    }
                    else -> {
                        adapter.addBootOptions(bootOptions[6]) // Simple reboot
                    }
                }
            }
            else -> {
                adapter.addBootOptions(bootOptions[6]) // Simple reboot
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.swap -> {
                when {
                    Root().check() -> {
                        LatteSwitchCom().execute()
                        adapter.clear()
                        init()
                        loadPreferences()

                        // Setting custom toast
                        val toast = Toast.makeText(this, R.string.reboot_device_title, Toast.LENGTH_LONG)
                        toast.view = layoutInflater.inflate(R.layout.toast,findViewById(R.id.toast))
                        toast.setGravity(Gravity.CENTER,0,0)

                        val view = toast.view
                        view?.setBackgroundResource(R.drawable.toast_background)
                        val title = view?.findViewById<TextView>(R.id.title)
                        val image = view?.findViewById<ImageView>(R.id.image)

                        when {
                            BootFile().check() -> {
                                title?.text = getString(R.string.reboot_win_title)
                                image?.setBackgroundResource(R.drawable.ic_windows)
                                toast.show()
                            }
                            !BootFile().check() -> {
                                title?.text = getString(R.string.reboot_and_title)
                                image?.setBackgroundResource(R.drawable.ic_android)
                                toast.show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                    }
                }
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