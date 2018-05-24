package in.jivanmuktas.www.marg.firebase;

import android.util.Log;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by developer on 30-Oct-17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FBInstanceIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String instantId = FirebaseInstanceId.getInstance().getId();
        Log.d(TAG, "!!!!Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.


       //sendRegistrationToServer(refreshedToken,instantId);
    }
    private void sendRegistrationToServer(String token,String Id) {
        // TODO: Implement this method to send token to your app server.

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("server/saving-data/device_Key");
        DatabaseReference ref2 = database.getReference("server/saving-data/Instant_Id");
        // then store your token ID
        ref1.push().setValue(token);
        ref2.push().setValue(Id);
    }
}
