package com.notme.second.universal.services.user.po;

import com.notme.second.universal.DatabaseDataObject;

/**
 * @author listen
 * 用户信息，读多写多的属性
 **/
public class UserInfo extends DatabaseDataObject {

    // 主键Id
    private Long id;

    // 人造外键
    private String userId;

    // 用户头像Url，上传后放入到一个server（file system也算）
    private String avatarUrl;

    // 昵称
    private String nickName;

    // 签名简介
    private String description;

    // 扩展信息
    private String ext;

}
