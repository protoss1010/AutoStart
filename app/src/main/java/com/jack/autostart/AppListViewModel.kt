package com.jack.autostart

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.jack.autostart.app.BaseApplication
import com.jack.autostart.utils.GsonUtils
import com.jack.autostart.utils.SharedPrefsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val AUTO_START_KEY = "AUTO_START_APPS"

class AppListViewModel : ViewModel() {

    val appsInfo = mutableStateListOf<AppInfo>()
    val selectedAppsInfo = mutableStateListOf<AppInfo>()

    fun getAllInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllInstalledApps()
        }
    }

    private fun fetchAllInstalledApps() {
        val packageManager = BaseApplication.context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resolveInfoList) {
            val appInfo = AppInfo(
                appName = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager),
            )
            appsInfo.add(appInfo)
        }
    }

    fun addSelectItem(appInfo: AppInfo) {
        if (!selectedAppsInfo.any { it == appInfo }) {
            selectedAppsInfo.add(appInfo)
        }
        saveSelectedAppsInfo(selectedAppsInfo)
    }

    fun removeSelectItem(appInfo: AppInfo) {
        selectedAppsInfo.remove(appInfo)
        saveSelectedAppsInfo(selectedAppsInfo)
    }

    private fun saveSelectedAppsInfo(selectedAppsInfo: List<AppInfo>) {
        SharedPrefsUtils.setStringPreference(AUTO_START_KEY, GsonUtils.toJson(selectedAppsInfo))
    }

    fun getSelectedAppsInfo(): List<AppInfo> {
        return GsonUtils.fromJson(
            SharedPrefsUtils.getStringPreference(
                AUTO_START_KEY,
                GsonUtils.toJson(mutableListOf<AppInfo>())
            ),
            object : TypeToken<List<AppInfo>>() {}.type
        )
    }

    data class AppInfo(
        val appName: String,
        val packageName: String,
        @Transient val icon: Drawable,
    )

}