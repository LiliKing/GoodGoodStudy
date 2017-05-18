package com.smk.view.showitemtouch.bean;

import java.util.List;

/**
 * 主展台
 */
public class Stands {
    private String expier;//有效期	string
    private List<SubStands> subStands;//子展台列表	object

    public String getExpier() {
        return expier;
    }

    public void setExpier(String expier) {
        this.expier = expier;
    }

    public List<SubStands> getSubStands() {
        return subStands;
    }

    public void setSubStands(List<SubStands> subStands) {
        this.subStands = subStands;
    }
}
