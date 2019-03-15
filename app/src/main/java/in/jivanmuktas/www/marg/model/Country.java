package in.jivanmuktas.www.marg.model;

public class Country {
    String country_id,country_name;

    public Country(String country_id, String country_name) {
        this.country_id = country_id;
        this.country_name = country_name;
    }

    public Country() {
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    @Override
    public String toString() {
        return country_name;
    }
}
