package com.notme.second.universal.services.user.po;

import com.notme.second.universal.DatabaseDataObject;

/**
 * @author listen
 * 作为读多写少的数据封装而存在，唯一标志一名记录在库的用户
 **/
public class User extends DatabaseDataObject {

    // 用户Id
    private Long id;

    // 用户密码（加密后）
    private String pwd;

    // 加密方式，暂定字符串
    private String encryptType;

    // 用户信息Id，人造外键
    private String userInfoId;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", pwd='" + pwd + '\'' +
                ", encryptType='" + encryptType + '\'' +
                ", userInfoId='" + userInfoId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
