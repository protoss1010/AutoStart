package com.jack.autostart.utils;

import android.content.Context;
import android.content.Intent;

import com.jack.autostart.app.BaseApplication;

public class AppLauncherUtils {

    public static void launchAppWithPackageName(String packageName) {
        Context context = BaseApplication.getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}