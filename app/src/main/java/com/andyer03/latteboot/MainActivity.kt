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

            // Load the main code
            init()
        }
    }

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
            R.drawable.ic_safe_mode, // Safemode
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
            getString(R.string.reboot_android_title), // Android
            getString(R.string.reboot_windows_title) // Windows
        )

        // Define current bootloader
        val currentBootloader = arrayOf (
            "", // Nothing
            getString(R.string.boot_item_current) // Current bootloader
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

        // Filling adapter
        adapter.addBootOptions(bootOptions[0]) // Screen off
        adapter.addBootOptions(bootOptions[1]) // Shutdown
        adapter.addBootOptions(bootOptions[2]) // Recovery
        adapter.addBootOptions(bootOptions[3]) // Bootloader
        adapter.addBootOptions(bootOptions[4]) // DNX
        adapter.addBootOptions(bootOptions[5]) // Safemode

        when (Root().check()) {
            true -> {
                when (BootFile().check()) {
                    "Windows" -> {
                        adapter.addBootOptions(bootOptions[7]) // Android
                        adapter.addBootOptions(bootOptions[10]) // Current bootloader Windows
                    }
                    "Android" -> {
                        adapter.addBootOptions(bootOptions[9]) // Current bootloader Android
                        adapter.addBootOptions(bootOptions[8]) // Windows
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
            ComponentName(applicationContext, RebootRecovery::class.java),  // 0 Recovery
            ComponentName(applicationContext, RebootWindows::class.java),   // 1 Windows
        )

        when (item.itemId) {
            // Show or hide shortcuts from app drawer
            R.id.bootloader_swap -> {
                when {
                    Root().check() -> {
                        LatteSwapBoot().swap() // Swap boot files
                        adapter.clear() // Clear adapter
                        init() // Recreating adapter
                        swapToast() // Show toast after swap
                    }
                    else -> {
                        Toast.makeText(this, R.string.unavailable_title, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.recovery_shortcut -> {
                when (sp.getBoolean("recovery_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[0],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("recovery_switch", false)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_disabled_toast), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.windows_shortcut -> {
                when (sp.getBoolean("windows_switch", false)) {
                    false -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", true)
                        spEditor.apply()
                        Toast.makeText(this, getString(R.string.launcher_icon_enabled_toast), Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        p.setComponentEnabledSetting(
                            preferences[1],
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        spEditor.putBoolean("windows_switch", false)
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
                title?.text = getString(R.string.reboot_windows_title)
                image?.setBackgroundResource(R.drawable.ic_windows)
                toast.show()
            }
            BootFile().check() == "Android" -> {
                title?.text = getString(R.string.reboot_android_title)
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