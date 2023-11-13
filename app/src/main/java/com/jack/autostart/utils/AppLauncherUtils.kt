package com.jack.autostart.utils

import android.content.Context

object AppLauncherUtils {

    fun launchAppWithPackageName(context: Context, packageName: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }

}