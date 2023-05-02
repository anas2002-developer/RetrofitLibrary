package com.example.retrofit.ModelResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchUsersResponse {

    @SerializedName("users")
    List<User> userList;
    String error;

    public FetchUsersResponse(List<User> userList, String error) {
        this.userList = userList;
        this.error = error;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
