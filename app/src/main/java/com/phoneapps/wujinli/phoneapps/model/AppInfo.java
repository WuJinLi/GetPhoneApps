package com.phoneapps.wujinli.phoneapps.model;

import android.graphics.drawable.Drawable;

/**
 * author: WuJinLi
 * time  : 17/5/26
 * desc  :
 */

public class AppInfo {
    private String appName;
    private Drawable appIcon;
    private long lastUpdateTime;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
