package com.dbtapps.chatroom.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    public String name;
    public String password;
    public String phone_number;
    public String profile_picture;
    public String user_id;

    public UserModel(String name, String password, String phone_number, String profile_picture, String user_id) {
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
        this.profile_picture = profile_picture;
        this.user_id = user_id;
    }
}
