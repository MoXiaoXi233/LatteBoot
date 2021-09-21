package com.andyer03.latteboot

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.andyer03.latteboot.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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

        when (Root().check()) {
            true -> {
                when (BootFile().check()) {
                    "Windows" -> {
                        adapter.addBootOptions(bootOptions[9]) // Reboot Windows
                        adapter.addBootOptions(bootOptions[8]) // Android
                    }
                    "Android" -> {
                        adapter.addBootOptions(bootOptions[10]) // Reboot Android
                        adapter.addBootOptions(bootOptions[7]) // Windows
                    }
                    "Error" -> {
                        // None
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
        val dsp = PreferenceManager.getDefaultSharedPreferences(this)
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val spEditor: SharedPreferences.Editor = sp.edit()

        val background = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.defaultLayout)

        dsp.registerOnSharedPreferenceChangeListener { _, _ ->
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

            // Changing app theme
            when (dsp.getBoolean("theme", false)) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    background.background = ResourcesCompat.getDrawable(resources, R.color.black, null)
                    spEditor.putBoolean("theme", true)
                    spEditor.apply()
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    background.background = ResourcesCompat.getDrawable(resources, R.color.orange_dark, null)
                    spEditor.putBoolean("theme", false)
                    spEditor.apply()
                }
            }

            // Changing title depending on bootloader
            when (dsp.getBoolean("window_title", false)) {
                true -> {
                    changeTitle()
                    spEditor.putBoolean("window_title", true)
                    spEditor.apply()
                }
                false -> {
                    title = getString(R.string.app_name)
                    spEditor.putBoolean("window_title", false)
                    spEditor.apply()
                }
            }

            // Changing status bar color depending on bootloader
            when {
                BootFile().check() == "Windows" && dsp.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
                    spEditor.putBoolean("status_bar", true)
                    spEditor.apply()
                }
                BootFile().check() == "Android" && dsp.getBoolean("status_bar", false) -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
                    spEditor.putBoolean("status_bar", true)
                    spEditor.apply()
                }
                else -> {
                    window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
                    spEditor.putBoolean("status_bar", false)
                    spEditor.apply()
                }
            }

            // Show toast after swapping boot files
            when (dsp.getBoolean("toast", false)) {
                true -> {
                    spEditor.putBoolean("toast", true)
                    spEditor.apply()
                }
                false -> {
                    spEditor.putBoolean("toast", false)
                    spEditor.apply()
                }
            }

            // Show or hide shortcuts from app drawer
            when (dsp.getBoolean("reboot", false)) {
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
            when (dsp.getBoolean("screen_off", false)) {
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
            when (dsp.getBoolean("recovery", false)) {
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
            when (dsp.getBoolean("fastboot", false)) {
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
            when (dsp.getBoolean("dnx", false)) {
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
            when (dsp.getBoolean("power_down", false)) {
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
            when (dsp.getBoolean("safe_mode", false)) {
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
            when (dsp.getBoolean("windows", false)) {
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
            when (dsp.getBoolean("android", false)) {
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
            Root().check() && BootFile().check() == "Windows" -> {
                title = getString(R.string.next_boot_windows)

            }
            Root().check() && BootFile().check() == "Android" -> {
                title = getString(R.string.next_boot_android)
            }
        }
    }

    // Loading preferences
    private fun loadPreferences() {
        val window = this@MainActivity.window
        val activity = this@MainActivity
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val background = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.defaultLayout)

        // Changing app theme
        when (sp.getBoolean("theme", false)) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                background.background = ResourcesCompat.getDrawable(resources, R.color.black, null)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                background.background = ResourcesCompat.getDrawable(resources, R.color.orange_dark, null)
            }
        }

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
            BootFile().check() == "Windows" && sp.getBoolean("status_bar", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.blue)
            }
            BootFile().check() == "Android" && sp.getBoolean("status_bar", false) -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.green)
            }
            else -> {
                window.statusBarColor = ContextCompat.getColor(activity, R.color.orange_dark)
            }
        }
    }
    
    // Show toast after swapping boot files
    private fun swapToast() {
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
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
                    BootFile().check() == "Windows" -> {
                        title?.text = getString(R.string.reboot_win_title)
                        image?.setBackgroundResource(R.drawable.ic_windows)
                        toast.show()
                    }
                    BootFile().check() == "Android" -> {
                        title?.text = getString(R.string.reboot_and_title)
                        image?.setBackgroundResource(R.drawable.ic_android)
                        toast.show()
                    }
                    BootFile().check() == "Error" -> {
                        title?.text = getString(R.string.error_title)
                        image?.setBackgroundResource(R.drawable.ic_bootloader)
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