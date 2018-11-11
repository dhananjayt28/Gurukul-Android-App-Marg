package in.jivanmuktas.www.marg.model;

public class IasCoaching {
    private String coaching_date;
    private String subject_name;

    public IasCoaching() {
    }

    public IasCoaching(String coaching_date, String subject_name) {
        this.coaching_date = coaching_date;
        this.subject_name = subject_name;
    }

    public String getCoaching_date() {
        return coaching_date;
    }

    public void setCoaching_date(String coaching_date) {
        this.coaching_date = coaching_date;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }
}
