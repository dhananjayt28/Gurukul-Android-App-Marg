package in.jivanmuktas.www.marg.constant;


public class Constant {
   public static final String BASE_URL = "http://jivanmuktas.tangenttechsolutions.com/Gurukul_App_Services.svc/";//Live
   public static final String BASE_URL_API = "http://jivanmuktasapi.ttssupport.info/api/";//Testing
   public static final String BASE_URL_NEW = "http://gurukulweb.tangenttechsolutions.com/api/";
   //public static final String BASE_URL = "http://testgurukul.tmlcsr.com/Gurukul_App_Services.svc/";//Testing

   public static final String LOGIN = BASE_URL + "user-login";

   public static final String Registration = BASE_URL + "user-registration";

   //public static final String ProfileView = BASE_URL + "get-user-profile";
   public static final String ProfileView = BASE_URL_NEW + "get-user-data";
   //public static final String GET_USER_DATA = BASE_URL_NEW + "get-user-data";

   public static final String ProfileUpdate = BASE_URL + "user-profile-update";

   public static final String AvailableEvent = BASE_URL + "get-event-data?eventid=";
   public static final String ApprovedEvent = BASE_URL_NEW + "get-approved-event-data?User_id=";
   public static final String RejectEvent = BASE_URL_NEW + "get-rejected-event-data?User_id=";

   public static final String PASSWORD_RESET = BASE_URL + "password-reset";
   public static final String PASSWORD_UPDATE = BASE_URL + "password-update";
   public static final String GET_SATSANG_CHAPTER = BASE_URL + "get-satsang-chapter-data?id=";

   public static final String GET_COUNTRY_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER COUNTRY";

   public static final String GET_CITY_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER CITY";

   public static final String EVENT_REGISTRTION = BASE_URL_NEW + "event-registration";
   public static final String GET_SUBJECT = BASE_URL + "get-education-data?educationid=";

   public static final String CANCEL_EVENT = BASE_URL + "event-registration-cancel";

   public static final String EVENT_MODIFICATION = BASE_URL + "event-registration-modification";

   public static final String VOLUNTEER_EVENT_APPROVE = BASE_URL + "get-volunteer-event-approved-data?";

   public static final String VOLUNTEER_EVENT_CHECKINOUT_UPDATE = BASE_URL + "volunteer-event-checkinout-update?eid=";

   public static final String NIVRITTI_REQUIRE_VOLUNTEER = BASE_URL_API + "get-nivritti-require-volunteer/";

   public static final String GET_COACHING_DATA = BASE_URL_NEW +"get-coaching-detail-data";

   public static final String GET_EVENT_CALENDAR_BREAKUP_DATA = BASE_URL_NEW + "get-event-calendar-breakup-data?event_id=" ;

   public static final String GET_EDUCATION_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER EDUCATION";

}
