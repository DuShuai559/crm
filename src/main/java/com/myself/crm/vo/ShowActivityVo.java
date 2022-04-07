package com.myself.crm.vo;

import java.util.List;

/**
 * Create by DS on 2022/04/07 13:25
 */
public class ShowActivityVo<T> {
    private Integer countSize;
    private List<T> activityData;

    public Integer getCountSize() {
        return countSize;
    }

    public void setCountSize(Integer countSize) {
        this.countSize = countSize;
    }

    public List<T> getActivityData() {
        return activityData;
    }

    public void setActivityData(List<T> activityData) {
        this.activityData = activityData;
    }
}
