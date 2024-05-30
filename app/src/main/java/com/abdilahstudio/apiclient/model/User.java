package com.abdilahstudio.apiclient.model;

import com.abdilahstudio.apiclient.response.ApiClient;

public class User {
    private int id;
    private String name;
    private String email;
    private String photoUrl; // New field for photo URL
    private String tglLahir;

    public User(int id, String name, String email, String tglLahir, String photoUrl) {
        this.id = id;
        this.name = String.valueOf(name);
        this.email = email;
        this.photoUrl = photoUrl;
        this.tglLahir = tglLahir;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        String url = new ApiClient().BASE_URL;
        return url + photoUrl;
    }

    public String getTglLahir(){
        return tglLahir;
    }

    // Setter untuk mengatur id user
    public void setId(int id) {
        this.id = id;
    }

    // Setter untuk mengatur nama user
    public void setName(String name) {
        this.name = name;
    }

    // Setter untuk mengatur email user
    public void setEmail(String email) {
        this.email = email;
    }

    // Setter untuk mengatur email user
    public void setPhotoUrl(String photoUrl) {
        this.email = email;
    }
}
