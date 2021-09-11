package com.andyer03.latteboot

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andyer03.latteboot.shortcuts.*
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        val preferenceScreen = PreferenceManager.getDefaultSharedPreferences(this)
        val p = packageManager
        val rebootDeviceComponentName = ComponentName(applicationContext, RebootDevice::class.java)
        val screenOffComponentName = ComponentName(applicationContext, ScreenOff::class.java)
        val rebootRecoveryComponentName = ComponentName(applicationContext, RebootRecovery::class.java)
        val rebootFastbootComponentName = ComponentName(applicationContext, RebootFastboot::class.java)
        val rebootDNXComponentName = ComponentName(applicationContext, RebootDNX::class.java)
        val powerDownComponentName = ComponentName(applicationContext, ShutDown::class.java)
        val rebootSafemodeComponentName = ComponentName(applicationContext, RebootSafeMode::class.java)
        val rebootWindowsComponentName = ComponentName(applicationContext, RebootWindows::class.java)
        val delayedRebootWindows = ComponentName(applicationContext, DelayedRebootWindows::class.java)

        // Show or hide Windows icon from app drawer

        when (preferenceScreen.getBoolean("reboot", false)) {
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
        when (preferenceScreen.getBoolean("screen_off", false)) {
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
        when (preferenceScreen.getBoolean("recovery", false)) {
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
        when (preferenceScreen.getBoolean("fastboot", false)) {
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
        when (preferenceScreen.getBoolean("dnx", false)) {
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
        when (preferenceScreen.getBoolean("power_down", false)) {
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
        when (preferenceScreen.getBoolean("safe_mode", false)) {
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
        when (preferenceScreen.getBoolean("windows", false)) {
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
        when (preferenceScreen.getBoolean("delayedRebootWindows", false)) {
            true -> {
                p.setComponentEnabledSetting(
                    delayedRebootWindows,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
            false -> {
                p.setComponentEnabledSetting(
                    delayedRebootWindows,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}