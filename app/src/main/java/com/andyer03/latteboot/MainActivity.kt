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
import com.andyer03.latteboot.*
import com.andyer03.latteboot.commands.*
import com.andyer03.latteboot.other.Device
import com.andyer03.latteboot.shortcuts.*
import java.util.*

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = BootAdapter()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Device().check()) {
            Toast.makeText(this, R.string.mi_pad_2_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
        else {
            val label = findViewById<TextView>(R.id.label)
            label.text = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " " + getString(R.string.about_title)

            val swapBootloader = findViewById<TextView>(R.id.swapBootloader)
            swapBootloader.setOnClickListener {
                when {
                    Root().check() -> {
                        LatteSwitchCom().execute() // Swap boot files
                        adapter.clear() // Clear adapter
                        init() // Recreating adapter
                        swapToast() // Show toast after swap
                    }
                    else -> {
                        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Changing title depending on bootloader
            title = "Shortcuts"

            // Load the main code
            init()
        }
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
        val p = packageManager
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val spEditor: SharedPreferences.Editor = sp.edit()

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

        when (item.itemId) {
            // Show or hide shortcuts from app drawer
            R.id.reboot_switch -> {
                when (sp.getBoolean("reboot_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("reboot_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("reboot_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.power_switch -> {
                when (sp.getBoolean("power_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("power_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("power_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.recovery_switch -> {
                when (sp.getBoolean("recovery_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[2],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[2],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.bootloader_switch -> {
                when (sp.getBoolean("bootloader_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[3],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("bootloader_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[3],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("bootloader_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.dnx_switch -> {
                when (sp.getBoolean("dnx_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[4],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("dnx_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[4],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("dnx_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.power_down_switch -> {
                when (sp.getBoolean("power_down_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[5],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("power_down_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[5],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("power_down_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.safe_mode_switch -> {
                when (sp.getBoolean("safe_mode_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[6],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("safe_mode_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[6],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("safe_mode_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.windows_switch -> {
                when (sp.getBoolean("windows_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[7],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[7],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.android_switch -> {
                when (sp.getBoolean("android_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[8],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("android_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[8],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("android_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    // Show toast after swapping boot files
    private fun swapToast() {
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