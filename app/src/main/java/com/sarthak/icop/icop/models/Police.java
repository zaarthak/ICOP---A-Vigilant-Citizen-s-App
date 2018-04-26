package com.sarthak.icop.icop.models;

public class Police {

    private int id;
    private String name;
    private String post;
    private String phone;

    public Police() {
    }

    public Police(int id, String name, String post, String phone) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
