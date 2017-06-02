package com.ff.wxzs.domain;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class UserInfo {
    public int uid;
    private String name;

    public String getNickname() {
        if(nickname == null){
            nickname = "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getShowName(){
        return getNickname().equals("") ? getName() : getNickname();
    }

    private String nickname;
    private String desc;
    private int type;

    public boolean isCanTry() {
        return canTry;
    }

    public void setCanTry(boolean canTry) {
        this.canTry = canTry;
    }

    private boolean canTry = true;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
