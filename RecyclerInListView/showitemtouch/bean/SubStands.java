package com.smk.view.showitemtouch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 子展台列表
 */
public class SubStands implements Serializable {
    private List<ItemBean> apply;//应用	object

    private String subName;//子展台名称	string

    public List<ItemBean> getApply() {
        return apply;
    }

    public void setApply(List<ItemBean> apply) {
        this.apply = apply;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
