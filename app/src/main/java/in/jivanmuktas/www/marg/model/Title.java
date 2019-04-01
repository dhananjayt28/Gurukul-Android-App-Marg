package in.jivanmuktas.www.marg.model;

public class Title {
    String title_id,title_name;

    public Title() {
    }

    public Title(String title_id, String title_name) {
        this.title_id = title_id;
        this.title_name = title_name;
    }

    public String getTitle_id() {
        return title_id;
    }

    public void setTitle_id(String title_id) {
        this.title_id = title_id;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    @Override
    public String toString() {
        return title_name;
    }
}
