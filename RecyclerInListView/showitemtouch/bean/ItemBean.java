package com.smk.view.showitemtouch.bean;

import java.io.Serializable;

/**
 * 展台的一个小组件
 */
public class ItemBean implements Serializable {
    private String appId;//应用id
    private String appName;//	用户名
    private String destination;//目的地
    private String enabled;//应用是否可用 on;off
    private String icon;//图标

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}