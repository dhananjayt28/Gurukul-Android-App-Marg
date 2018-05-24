package in.jivanmuktas.www.marg.dataclass;

import java.util.HashMap;

/**
 * Created by developer on 14-Feb-18.
 */

public class UserData {

    public static String getTitle(String title){
        HashMap<String,String> titleList = new HashMap<>();
        titleList.put("1","Mr.");
        titleList.put("2","Mrs.");
        titleList.put("3","Miss.");
        return titleList.get(title);
    }

    public static String getGender(String gender){
        HashMap<String,String> genderList = new HashMap<>();
        genderList.put("0","Male");
        genderList.put("1","Female");
        return genderList.get(gender);
    }

    public static String getCountry(String country){
        HashMap<String,String> countryList = new HashMap<>();
        countryList.put("1","Bahrain");
        countryList.put("2","Dubai");
        countryList.put("3","India - ex Mumbai");
        countryList.put("4","Indonesia");
        countryList.put("5","Mumbai");
        countryList.put("6","Muscat");
        countryList.put("7","Singapore");
        countryList.put("8","Thailand");
        countryList.put("9","USA");
        return countryList.get(country);
    }

    public static String getEducation(String Education){
        HashMap<String,String> educationList = new HashMap<>();
        educationList.put("1","Graduate");
        educationList.put("2","Post graduate");
        educationList.put("3","Professional");
        educationList.put("4","Other");
        return educationList.get(Education);
    }
}
