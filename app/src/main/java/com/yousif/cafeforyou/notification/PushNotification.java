package com.yousif.cafeforyou.notification;

public class PushNotification {

    NotificationData data;
    String to;

    public PushNotification() {
    }

    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
