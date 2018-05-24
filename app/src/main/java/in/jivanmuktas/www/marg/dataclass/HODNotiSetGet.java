package in.jivanmuktas.www.marg.dataclass;

/**
 * Created by developer on 31-Oct-17.
 */

public class HODNotiSetGet {
    public String id;
    String notifcation;
    String date;
    String mobile;
    String email;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotifcation() {
        return notifcation;
    }

    public void setNotifcation(String notifcation) {
        this.notifcation = notifcation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
