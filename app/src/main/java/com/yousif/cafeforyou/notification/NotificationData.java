package com.yousif.cafeforyou.notification;

public class NotificationData {
    String title;
    String message;

    public NotificationData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public NotificationData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
