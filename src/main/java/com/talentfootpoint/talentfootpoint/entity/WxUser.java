package com.talentfootpoint.talentfootpoint.entity;

import java.io.Serializable;

/**
 * 用户表
 * @author lsb
 *
 */
public class WxUser implements Serializable {

    private static final long serialVersionUID = -3219658250633723822L;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 小程序唯一识别码
     */

    private String openId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     *  用户手机号码（登陆账号）
     */
    private String phone;

    /**
     * 微信用户昵称
     */
    private String appNickName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String appId) {
        this.openId = appId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
