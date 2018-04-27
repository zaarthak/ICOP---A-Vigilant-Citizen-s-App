package com.sarthak.icop.icop.models;

import java.io.Serializable;

public class Report implements Serializable {

    private String id;
    private String category;
    private String information;
    private String contact;
    private String status;

    public Report() {
    }

    public Report(String id, String category, String information, String contact, String status) {
        this.id = id;
        this.category = category;
        this.information = information;
        this.contact = contact;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
