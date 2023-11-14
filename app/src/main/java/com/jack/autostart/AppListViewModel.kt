package com.jack.autostart

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
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
        for (launchList in getSelectedAppsInfo()) {
            selectedAppsInfo.add(AppInfo(launchList.appName, launchList.packageName))
        }
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
            for ((index, itemSelected) in selectedAppsInfo.withIndex()) {
                if (itemSelected == appInfo) {
                    appInfo.order = index + 1
                }
            }
            appsInfo.add(appInfo)
        }
    }

    fun addSelectItem(appInfo: AppInfo) {
        if (!selectedAppsInfo.any { it == appInfo }) {
            selectedAppsInfo.add(appInfo)
        }
        updateApps()
        saveSelectedAppsInfo()
    }

    fun removeSelectItem(appInfo: AppInfo) {
        selectedAppsInfo.remove(appInfo)
        updateApps()
        saveSelectedAppsInfo()
    }

    private fun updateApps() {
        appsInfo.forEach { info -> info.order = -1 }
        for (info in appsInfo) {
            for ((index, itemSelected) in selectedAppsInfo.withIndex()) {
                if (itemSelected == info) {
                    info.order = index + 1
                }
            }
        }
    }

    private fun saveSelectedAppsInfo() {
        val mutableListOf = mutableListOf<LaunchList>()
        for (launchList in selectedAppsInfo) {
            mutableListOf.add(LaunchList(launchList.appName, launchList.packageName))
        }
        SharedPrefsUtils.setStringPreference(AUTO_START_KEY, GsonUtils.toJson(selectedAppsInfo))
    }

    fun getSelectedAppsInfo(): List<LaunchList> {
        return GsonUtils.fromJson(
            SharedPrefsUtils.getStringPreference(
                AUTO_START_KEY,
                GsonUtils.toJson(mutableListOf<LaunchList>())
            ),
            object : TypeToken<List<LaunchList>>() {}.type
        )
    }

    data class LaunchList(
        val appName: String,
        val packageName: String
    )

    data class AppInfo(
        val appName: String,
        val packageName: String,
        @Transient val icon: Drawable? = null,
    ) {
        var order by mutableIntStateOf(-1)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AppInfo) return false
            if (appName != other.appName) return false
            if (packageName != other.packageName) return false
            return true
        }

        override fun hashCode(): Int {
            var result = appName.hashCode()
            result = 31 * result + packageName.hashCode()
            return result
        }

    }

}