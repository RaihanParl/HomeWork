package com.bidjidevelops.hd.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by You on 20/04/2017.
 */

public class GsonUser {
    @SerializedName("user")
    public List<User> DataUser;

    @SerializedName("result")
    public Boolean result;

    @SerializedName("msg")
    public String msg;

    public class User {
        @SerializedName("id")
        public Object id;

        @SerializedName("Image")
        public String imageUser;

        @SerializedName("email")
        public String emailUser;

        @SerializedName("Username")
        public String usernameUser;

        @SerializedName("password")
        public String passwordUser;

        @SerializedName("School")
        public String schoolUser;

    }


}
