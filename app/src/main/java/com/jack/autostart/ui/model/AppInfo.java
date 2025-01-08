package com.jack.autostart.ui.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private final String mAppName;
    private final String mPackageName;
    private final transient Drawable mIcon;
    private int mOrder = AppListViewModel.NO_ORDER;
    private int mDelaySec = AppListViewModel.DEFAULT_DELAY_SEC;

    public AppInfo(String appName, String packageName, Drawable icon) {
        mAppName = appName;
        mPackageName = packageName;
        mIcon = icon;
    }

    public String getAppName() {
        return mAppName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public int getDelaySec() {
        return mDelaySec;
    }

    public void setDelaySec(int delaySec) {
        mDelaySec = delaySec;
    }

    public void decrementDelaySec() {
        mDelaySec -= 1;
    }

    public void incrementDelaySec() {
        mDelaySec += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppInfo appInfo = (AppInfo) o;
        if (!mAppName.equals(appInfo.mAppName)) return false;
        return mPackageName.equals(appInfo.mPackageName);
    }

    @Override
    public int hashCode() {
        int result = mAppName.hashCode();
        result = 31 * result + mPackageName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "mAppName='" + mAppName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mIcon=" + mIcon +
                ", mOrder=" + mOrder +
                ", mDelaySec=" + mDelaySec +
                '}';
    }
}
