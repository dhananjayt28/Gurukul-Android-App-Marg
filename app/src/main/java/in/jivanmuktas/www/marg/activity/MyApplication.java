package in.jivanmuktas.www.marg.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyApplication extends Application{
    public String AppKey = "4321";
    private String userId, userName,password,education,dob,age,gender,contact,email,country,chapter,roleId,city;
    public boolean session=false;
    public SharedPreferences pref;
    private static MyApplication instance;

    private enum UserData {
        LOGIN_PREF,SESSION,USER_ID,USER_NAME,PASSWORD,EDUCATION,DOB,AGE,GENDER,CONTACT, EMAIL,COUNTRY,CHAPTER,ROLEID,CITY
    }
    public static MyApplication getInstance(){
        return instance;
    }
	@Override
	public void onCreate() {
		super.onCreate();
        instance = MyApplication.this;
		pref = getSharedPreferences(UserData.LOGIN_PREF.name(), Context.MODE_PRIVATE);
	}
    public boolean isSession() {
        return pref.getBoolean(UserData.SESSION.name(), this.session);
    }

    public void setSession(boolean session) {
        this.session = session;
        Editor edit = pref.edit();
        edit.putBoolean(UserData.SESSION.name(), this.session);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getUserId() {
        return pref.getString(UserData.USER_ID.name(), this.userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
        Editor edit = pref.edit();
        edit.putString(UserData.USER_ID.name(), this.userId);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getUserName() {
        return pref.getString(UserData.USER_NAME.name(), this.userName);
    }

    public void setUserName(String userName) {
        this.userName = userName;
        Editor edit = pref.edit();
        edit.putString(UserData.USER_NAME.name(), this.userName);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getPassword() {
        return pref.getString(UserData.PASSWORD.name(),this.password);
    }
    public void setPassword(String password) {
        this.password = password;
        Editor editor=pref.edit();
        editor.putString(UserData.PASSWORD.name(),this.password);
        editor.commit();
    }
    ///////////******************************/////////////////
    public String getEducation() {
        return pref.getString(UserData.EDUCATION.name(),this.education);
    }

    public void setEducation(String education) {
        this.education = education;
        Editor editor=pref.edit();
        editor.putString(UserData.EDUCATION.name(),this.education);
        editor.commit();
    }
    ///////////******************************/////////////////
    public String getDob() {
        return pref.getString(UserData.DOB.name(), this.dob);
    }

    public void setDob(String dob) {
        this.dob = dob;
        Editor edit = pref.edit();
        edit.putString(UserData.DOB.name(), this.dob);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getAge() {
        return pref.getString(UserData.AGE.name(), this.age);
    }
    public void setAge(String age) {
        this.age = age;
        Editor edit = pref.edit();
        edit.putString(UserData.AGE.name(), this.age);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getGender() {
        return pref.getString(UserData.GENDER.name(),this.gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
        Editor edit = pref.edit();
        edit.putString(UserData.GENDER.name(), this.gender);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getContact() {
        return pref.getString(UserData.CONTACT.name(), this.contact);
    }

    public void setContact(String userContact) {
        contact = userContact;
        Editor edit = pref.edit();
        edit.putString(UserData.CONTACT.name(), this.contact);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getEmail() {
        return pref.getString(UserData.EMAIL.name(), this.email);
    }

    public void setEmail(String userMail) {
        email = userMail;
        Editor edit = pref.edit();
        edit.putString(UserData.EMAIL.name(), this.email);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getCountry() {
        return pref.getString(UserData.COUNTRY.name(), this.country);
    }

    public void setCountry(String country) {
        this.country = country;
        Editor edit = pref.edit();
        edit.putString(UserData.COUNTRY.name(), this.country);
        edit.commit();
    }
    ///////////******************************/////////////////
    public String getChapter() {
        return pref.getString(UserData.CHAPTER.name(), this.chapter);
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
        Editor edit = pref.edit();
        edit.putString(UserData.CHAPTER.name(), this.chapter);
        edit.commit();
    }

    public String getCity() {
        return pref.getString(UserData.CITY.name(), this.city);
    }

    public void setCity(String city) {
        this.city = city;
        Editor edit = pref.edit();
        edit.putString(UserData.CITY.name(), this.city);
        edit.commit();
    }
    ////////**********************************//////////////
    public String getRoleId(){
        return pref.getString(UserData.ROLEID.name() , this.roleId);
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
        Editor edit = pref.edit();
        edit.putString(UserData.ROLEID.name(), this.roleId);
        edit.commit();
    }

}
