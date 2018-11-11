package in.jivanmuktas.www.marg.model;

public class Education {
    String education_name;
    String education_id;

    public String getEducation_name() {
        return education_name;
    }

    public void setEducation_name(String education_name) {
        this.education_name = education_name;
    }

    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    @Override
    public String toString() {
        return education_name;
    }
}
