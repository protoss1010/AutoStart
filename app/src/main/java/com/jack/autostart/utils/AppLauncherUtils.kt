package com.jack.autostart.utils

import com.jack.autostart.app.BaseApplication

object AppLauncherUtils {

    fun launchAppWithPackageName(packageName: String) {
        val context = BaseApplication.context
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }

}