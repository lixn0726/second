package com.notme.second.universal;

/**
 * @author monstaxl
 * 数据库数据类，封装通用的数据
 **/
public class DatabaseDataObject {

    public String createTime;

    public String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
