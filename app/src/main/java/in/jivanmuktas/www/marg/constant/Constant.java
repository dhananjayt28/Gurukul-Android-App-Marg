package in.jivanmuktas.www.marg.constant;


public class Constant {
   //public static final String BASE_URL = "http://jivanmuktas.tangenttechsolutions.com/Gurukul_App_Services.svc/";//Live
   public static final String BASE_URL_API = "http://jivanmuktasapi.ttssupport.info/api/";//Testing
   //public static final String BASE_URL_NEW = "http://gurukulweb.tangenttechsolutions.com/api/";
   //public static final String BASE_URL = "http://testgurukul.tmlcsr.com/Gurukul_App_Services.svc/";//Testing
   public static final String BASE_URL_NEW = "http://uatappweb.jivanmuktas.org/api/"; //for api
  // public static final String BASE_URL = "http://uatappwebapi.jivanmuktas.org/Gurukul_App_Services.svc/"; //for service

   public static final String LOGIN = BASE_URL_NEW + "user-login";

   public static final String Registration = BASE_URL_NEW + "user-registration";

   //public static final String ProfileView = BASE_URL + "get-user-profile";
   public static final String ProfileView = BASE_URL_NEW + "get-user-data";
   public static final String GITADISTRIBUTION_ITERARY_CONFIRMATION_UPDATE = BASE_URL_NEW + "itinary-confirmation-update";
   public static final String GET_USER_DATA = BASE_URL_NEW + "get-user-data";

   public static final String ProfileUpdate = BASE_URL_NEW + "user-profile-update";   //in service 1

   public static final String AvailableEvent = BASE_URL_NEW + "get-event-data?event_type=";
   public static final String ApprovedEvent = BASE_URL_NEW + "get-approved-event-data?user_id=";
   public static final String ViewApprovedEvent = BASE_URL_NEW + "get-approved-event-data?user_id=";
   public static final String RejectEvent = BASE_URL_NEW + "get-rejected-event-data?user_id=";

   //public static final String PASSWORD_RESET = BASE_URL + "password-reset";     //in service 2
   //public static final String PASSWORD_UPDATE = BASE_URL + "password-update";   //inservice 3
   //public static final String GET_COUNTRY_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER_COUNTRY";
   public static final String GET_COUNTRY_LIST = BASE_URL_NEW + "get-country-data";
   public static final String GET_CITY_LIST = BASE_URL_NEW + "get-city-data?";
   public static final String GET_SATSANG_CHAPTER = BASE_URL_NEW + "get-chapter-data?";

   public static final String GET_ID_CARD_SPINNER = BASE_URL_NEW + "get-master-data-list?category_name=ID_Card_Type";

   public static final String EVENT_REGISTRTION = BASE_URL_NEW + "event-registration";
   public static final String GET_SUBJECT = BASE_URL_NEW + "get-education-data?educationid=";     //inservice 5

   public static final String CANCEL_EVENT = BASE_URL_NEW + "event-registration-cancel";

   //public static final String EVENT_MODIFICATION = BASE_URL + "event-registration-modification";   //inservice 6

   //public static final String VOLUNTEER_EVENT_APPROVE = BASE_URL + "get-volunteer-event-approved-data?";
   public static final String VOLUNTEER_EVENT_APPROVE = BASE_URL_NEW + "get-registered-event-data?";

   public static final String VOLUNTEER_EVENT_CHECKINOUT_UPDATE = BASE_URL_NEW + "volunteer-event-checkinout-update";

   public static final String NIVRITTI_REQUIRE_VOLUNTEER = BASE_URL_API + "get-nivritti-require-volunteer/";

   public static final String GET_COACHING_DATA = BASE_URL_NEW +"get-coaching-detail-data";

   public static final String GET_EVENT_CALENDAR_BREAKUP_DATA = BASE_URL_NEW + "get-event-calendar-breakup-data?event_id=" ;

   public static final String GET_EDUCATION_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER_EDUCATION";

   public static final String GET_TITLE_LIST = BASE_URL_NEW + "get-master-data-list?category_name=MASTER_TITLE";

   public static final String GET_REGISTERED_EVENT_DATA = BASE_URL_NEW + "get-registered-event-data";

   public static final String GET_CONTENT_DATA = BASE_URL_NEW + "get-content-data";

   public static final String TOPIC_STATUS_UPDATE = BASE_URL_NEW + "topic-status-update";

   public static final String TOPIC_COMPLETION_STATUS = BASE_URL_NEW +"get-master-data-list?category_name=TOPIC_COMPLETION_STATUS";

   public static final String UPDATE_ITENARY_STATUS = BASE_URL_NEW + "update-itinerary-status ";

   public static final String GET_ITINERARY_INFORMATION = BASE_URL_NEW + "get-itinerary-information?";

}
