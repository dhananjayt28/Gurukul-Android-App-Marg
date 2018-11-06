package in.jivanmuktas.www.marg.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import in.jivanmuktas.www.marg.dataclass.HODNotiSetGet;
import in.jivanmuktas.www.marg.activity.Notification;
import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.database.DataBase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by developer on 03-Nov-17.
 */

public class HODNotifiFrag extends Fragment {
    ListView hodNotiList;
    DataBase dataBase;
    TextView no_notification;
    public ProgressDialog prsDlg;
    ArrayList<HODNotiSetGet> notificationData = new ArrayList<HODNotiSetGet>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hodnotification, container, false);
        no_notification = (TextView) view.findViewById(R.id.no_notification);
        no_notification.setVisibility(View.GONE);
        hodNotiList = (ListView) view.findViewById(R.id.hodNotiList);
        hodNotiList.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");

        ref.child("HOD").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("!!!!dataSnapshot",dataSnapshot+"");

                    try {
                        String value = dataSnapshot.getValue().toString();
                        JSONObject object = new JSONObject(value);
                        Iterator<String> iterator = object.keys();
                        while (iterator.hasNext()) {
                            String currentKey = iterator.next();
                            JSONObject obj = object.getJSONObject(currentKey);
                            HODNotiSetGet hodNotiSetGet = new HODNotiSetGet();
                            hodNotiSetGet.setId(currentKey);
                            hodNotiSetGet.setNotifcation(obj.getString("MESSAGE"));
                            hodNotiSetGet.setDate(obj.getString("DATE"));
                            hodNotiSetGet.setMobile(obj.getString("MOBILE"));
                            hodNotiSetGet.setEmail(obj.getString("EMAIL"));
                            notificationData.add(hodNotiSetGet);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (notificationData.size() != 0) {
                        NotificationAdapter adapter = new NotificationAdapter();
                        hodNotiList.setAdapter(adapter);
                        no_notification.setVisibility(View.GONE);
                        hodNotiList.setVisibility(View.VISIBLE);
                    } else {
                        no_notification.setVisibility(View.VISIBLE);
                        hodNotiList.setVisibility(View.GONE);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        dataBase = new DataBase(getActivity());

        /*Cursor result = dataBase.displayHOD();
        if (result.getCount() != 0) {
            result.moveToLast();///// Last to first data fetch
            do {
                String Id = result.getString(0);
                String Notification = result.getString(1);
                String Date = result.getString(2);
                String Mobile = result.getString(3);
                String Email = result.getString(4);

                HODNotiSetGet hodNotiSetGet = new HODNotiSetGet();
                hodNotiSetGet.setId(Id);
                hodNotiSetGet.setNotifcation(Notification);
                hodNotiSetGet.setDate(Date);
                hodNotiSetGet.setMobile(Mobile);
                hodNotiSetGet.setEmail(Email);
                notificationData.add(hodNotiSetGet);
            } while (result.moveToPrevious());
        }*/




        //Delete Item From List Start
        /*hodNotiList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Notification")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int delckr = dataBase.deleteHODRow(notificationData.get(position).id);

                                if(delckr>0){
                                    Toast.makeText(getActivity(), "Notification Sucessfully deleted.", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(getActivity(),Notification.class);
                                getActivity().finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
                return false;
            }
        });*/
        //Delete Item From List End
    }

    //Adapter Start
    class NotificationAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return notificationData.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v=inflater.inflate(R.layout.hodnotilistview, parent, false);

            // ImageView iv = (ImageView)v.findViewById(R.id.cartimageView);
            TextView tv1=(TextView)v.findViewById(R.id.tv1);
            TextView tv2=(TextView)v.findViewById(R.id.tv2);
            ImageView call = (ImageView) v.findViewById(R.id.call);
            ImageView msg = (ImageView) v.findViewById(R.id.msg);
            ImageView email = (ImageView) v.findViewById(R.id.email);
            final HODNotiSetGet obj = notificationData.get(position);

            //Picasso.with(Notification.this).load(obj.getItemImg()).into(iv);
            tv1.setText(obj.getDate());
            tv2.setText(obj.getNotifcation());

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri=Uri.parse("tel:"+obj.getMobile());
                        Intent i=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(),"Sorry! your device not support this features.",Toast.LENGTH_LONG).show();
                    }
                }
            });
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Uri uri=Uri.parse("sms:"+obj.getMobile());
                        Intent i=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(),"Sorry! your device not support this features.",Toast.LENGTH_LONG).show();
                    }
                }
            });
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=Uri.parse("mailto:"+obj.getEmail()+"?subject=Contact");
                    Intent i=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
            });
            return v;
        }

    }
    //Adapter End
    

}