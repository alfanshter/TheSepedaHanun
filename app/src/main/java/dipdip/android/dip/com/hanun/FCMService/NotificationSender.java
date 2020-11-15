package dipdip.android.dip.com.hanun.FCMService;

import dipdip.android.dip.com.hanun.Network.Data;

public class NotificationSender {
    public Data data;
    public String to;

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {
    }
}
