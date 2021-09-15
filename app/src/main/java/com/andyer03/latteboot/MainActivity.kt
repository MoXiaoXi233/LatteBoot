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
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.other.Device
import com.andyer03.latteboot.shortcuts.*
import java.util.*

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
                System("mountEFI").boot()
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
                        LatteSwitchCom().execute() // Swap boot files
                        adapter.clear() // Clear adapter
                        init() // Recreating adapter
                        loadPreferences() // Loading preferences
                        swapToast() // Show toast after swap
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

            val preferences = arrayOf (
                ComponentName(applicationContext, RebootDevice::class.java),    // 0 Reboot
                ComponentName(applicationContext, ScreenOff::class.java),       // 1 Screen off
                ComponentName(applicationContext, RebootRecovery::class.java),  // 2 Recovery
                ComponentName(applicationContext, RebootFastboot::class.java),  // 3 Fastboot
                ComponentName(applicationContext, RebootDNX::class.java),       // 4 DNX
                ComponentName(applicationContext, ShutDown::class.java),        // 5 Shutdown
                ComponentName(applicationContext, RebootSafeMode::class.java),  // 6 Safe mode
                ComponentName(applicationContext, RebootWindows::class.java),   // 7 Windows
                ComponentName(applicationContext, RebootAndroid::class.java)    // 8 Android
            )

            // Changing title depending on bootloader
            when (settingsPreferences.getBoolean("window_title", false)) {
                true -> {
                    changeTitle()
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("window_title", true)
                    editor.apply()
                }
                false -> {
                    title = getString(R.string.app_name)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("window_title", false)
                    editor.apply()
                }
            }

            // Changing status bar color depending on bootloader
            when {
                BootFile().check() && settingsPreferences.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar", true)
                    editor.apply()
                }
                !BootFile().check() && settingsPreferences.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar", true)
                    editor.apply()
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("status_bar", false)
                    editor.apply()
                }
            }

            // Show toast after swapping boot files
            when (settingsPreferences.getBoolean("toast", false)) {
                true -> {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("toast", true)
                    editor.apply()
                }
                false -> {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("toast", false)
                    editor.apply()
                }
            }

            // Show or hide shortcuts from app drawer
            when (settingsPreferences.getBoolean("reboot", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[0],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[0],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("screen_off", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[1],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[1],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("recovery", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[2],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[2],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("fastboot", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[3],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[3],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("dnx", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[4],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[4],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("power_down", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[5],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[5],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("safe_mode", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[6],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[6],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("windows", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[7],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[7],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
            when (settingsPreferences.getBoolean("android", false)) {
                true -> {
                    p.setComponentEnabledSetting(
                        preferences[8],
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
                false -> {
                    p.setComponentEnabledSetting(
                        preferences[8],
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            }
        }
    }

    // Changing title
    private fun changeTitle() {
        when {
            Root().check() && BootFile().check() -> {
                title = getString(R.string.next_boot_windows)

            }
            Root().check() && !BootFile().check() -> {
                title = getString(R.string.next_boot_android)
            }
        }
    }

    // Loading preferences
    private fun loadPreferences() {
        val window = this@MainActivity.window
        val activity = this@MainActivity
        val sp = getSharedPreferences("window", Context.MODE_PRIVATE)

        // Changing title depending on bootloader
        when (sp.getBoolean("window_title", false)) {
            true -> {
                changeTitle()
            }
            false -> {
                title = getString(R.string.app_name)
            }
        }

        // Changing status bar color depending on bootloader
        when {
            BootFile().check() && sp.getBoolean("status_bar", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
            }
            !BootFile().check() && sp.getBoolean("status_bar", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
            }
            else -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
            }
        }
    }
    
    // Show toast after swapping boot files
    private fun swapToast() {
        val sp = getSharedPreferences("window", Context.MODE_PRIVATE)
        when (sp.getBoolean("toast", false)) {
            true -> {
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
        }
    }

    // About dialog
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