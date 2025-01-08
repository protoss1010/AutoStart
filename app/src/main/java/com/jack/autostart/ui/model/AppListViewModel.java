package com.jack.autostart.ui.model;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
import com.jack.autostart.app.BaseApplication;
import com.jack.autostart.utils.GsonUtils;
import com.jack.autostart.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppListViewModel extends ViewModel {

    private static final String TAG = AppListViewModel.class.getSimpleName();

    private static final String AUTO_START_KEY = "AUTO_START_APPS";

    public static final int NO_ORDER = 9999;
    public static final int DEFAULT_DELAY_SEC = 5;

    private final ArrayList<AppInfo> mAppInfos = new ArrayList<>();
    private final ArrayList<AppInfo> mSelectedAppsInfo = new ArrayList<>();

    public void fetchAllInstalledApps() {
        mAppInfos.clear();
        mSelectedAppsInfo.clear();

        PackageManager packageManager = BaseApplication.getContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<AppInfo> selectedAppsInfo = getSelectedAppsInfo();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo appInfo = new AppInfo(
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadIcon(packageManager));
            for (int index = 0; index < selectedAppsInfo.size(); index++) {
                AppInfo itemSelected = selectedAppsInfo.get(index);
                if (itemSelected.equals(appInfo)) {
                    mSelectedAppsInfo.add(appInfo);
                    appInfo.setOrder(index + 1);
                    appInfo.setDelaySec(itemSelected.getDelaySec());
                }
            }
            mAppInfos.add(appInfo);
        }
        sortAppsInfo();
    }

    public ArrayList<AppInfo> getAppInfos() {
        return mAppInfos;
    }

    public void addSelectItem(AppInfo appInfo) {
        if (!mSelectedAppsInfo.contains(appInfo)) {
            mSelectedAppsInfo.add(appInfo);
            appInfo.setOrder(mSelectedAppsInfo.size());
        }
        sortAppsInfo();
        saveSelectedAppsInfo(mSelectedAppsInfo);
    }

    public void removeSelectItem(AppInfo appInfo) {
        appInfo.setOrder(NO_ORDER);
        appInfo.setDelaySec(DEFAULT_DELAY_SEC);
        mSelectedAppsInfo.remove(appInfo);
        for (int i = 0; i < mSelectedAppsInfo.size(); i++) {
            mSelectedAppsInfo.get(i).setOrder(i + 1);
        }
        sortAppsInfo();
        saveSelectedAppsInfo(mSelectedAppsInfo);
    }

    public void minus(AppInfo appInfo) {
        for (AppInfo selectedAppsInfo : mSelectedAppsInfo) {
            if (selectedAppsInfo.equals(appInfo)) {
                if (selectedAppsInfo.getDelaySec() > 0) {
                    selectedAppsInfo.decrementDelaySec();
                }
            }
        }
        saveSelectedAppsInfo(mSelectedAppsInfo);
    }

    public void plus(AppInfo appInfo) {
        for (AppInfo selectedAppsInfo : mSelectedAppsInfo) {
            if (selectedAppsInfo.equals(appInfo)) {
                selectedAppsInfo.incrementDelaySec();
            }
        }
        saveSelectedAppsInfo(mSelectedAppsInfo);
    }

    private void sortAppsInfo() {
        mAppInfos.sort(Comparator.comparingInt(AppInfo::getOrder));
    }

    private void saveSelectedAppsInfo(ArrayList<AppInfo> selectedAppsInfo) {
        SharedPrefsUtils.setStringPreference(AUTO_START_KEY, GsonUtils.toJson(selectedAppsInfo));
    }

    public List<AppInfo> getSelectedAppsInfo() {
        String json = SharedPrefsUtils.getStringPreference(AUTO_START_KEY, GsonUtils.toJson(new ArrayList<>()));
        return GsonUtils.fromJson(json, new TypeToken<List<AppInfo>>() {}.getType());
    }

}