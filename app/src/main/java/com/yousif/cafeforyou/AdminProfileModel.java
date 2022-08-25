package com.yousif.cafeforyou;

public class AdminProfileModel {
String admin_name,admin_number,admin_location;

    public AdminProfileModel() {
    }

    public AdminProfileModel(String admin_name, String admin_number, String admin_location) {
        this.admin_name = admin_name;
        this.admin_number = admin_number;
        this.admin_location = admin_location;
    }



    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_number() {
        return admin_number;
    }

    public void setAdmin_number(String admin_number) {
        this.admin_number = admin_number;
    }

    public String getAdmin_location() {
        return admin_location;
    }

    public void setAdmin_location(String admin_location) {
        this.admin_location = admin_location;
    }
}
