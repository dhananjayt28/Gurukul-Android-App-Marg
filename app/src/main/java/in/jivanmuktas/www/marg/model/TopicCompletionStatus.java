package in.jivanmuktas.www.marg.model;

public class TopicCompletionStatus {

    private String status_id;
    private String status_name;

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    @Override
    public String toString() {
        return status_name;
    }
}
