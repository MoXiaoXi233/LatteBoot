package com.andyer03.latteboot.commands

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Root {
    private fun method1(): Boolean
    {
        val buildTags : String = android.os.Build.TAGS
        return buildTags.contains("test-keys")
    }

    private fun method2(): Boolean
    {
        for(path in arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"))
        {
            if(File(path).exists())
                return true
        }
        return false
    }

    private fun method3(): Boolean
    {
        var process : Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            BufferedReader(InputStreamReader(process.inputStream))
            true
        } catch(th : Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    private fun method4(): Boolean
    {
        return File("/system/app/Superuser.apk").exists()
    }

    private fun method5(): Boolean
    {
        return File("/system/app/SuperSU.apk").exists()
    }

    fun check() : Boolean
    {
        return (method1() || method2() || method3() || method4()) || method5()
    }
}