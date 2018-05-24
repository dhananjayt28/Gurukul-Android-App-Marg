package in.jivanmuktas.www.marg.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import in.jivanmuktas.www.marg.dataclass.GenNotiSetGet;
import in.jivanmuktas.www.marg.activity.Notification;
import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.database.DataBase;

import java.util.ArrayList;

import in.jivanmuktas.www.marg.dataclass.GenNotiSetGet;

/**
 * Created by developer on 03-Nov-17.
 */

public class GenNotifiFrag extends Fragment {
    ListView genNotiList;
    DataBase dataBase;
    TextView tip;
    public ProgressDialog prsDlg;
    ArrayList<GenNotiSetGet> notificationData = new ArrayList<GenNotiSetGet>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gennotification, container, false);
        genNotiList = (ListView) view.findViewById(R.id.genNotiList);
        tip = (TextView)  view.findViewById(R.id.tip);
        tip.setVisibility(View.GONE);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataBase = new DataBase(getActivity());

        Cursor result = dataBase.displayGeneral();
        if (result.getCount() != 0) {
            result.moveToLast();///// Last to first data fetch
            do {
                String Id = result.getString(0);
                String Notification = result.getString(1);
                String Date = result.getString(2);

                GenNotiSetGet genNotiSetGet = new GenNotiSetGet();
                genNotiSetGet.setId(Id);
                genNotiSetGet.setNotifcation(Notification);
                genNotiSetGet.setDate(Date);
                notificationData.add(genNotiSetGet);
            } while (result.moveToPrevious());
        }
        if(notificationData.size() != 0) {
            tip.setVisibility(View.VISIBLE);
            NotificationAdapter adapter = new NotificationAdapter();
            genNotiList.setAdapter(adapter);
            //genNotiList.
        }else {
            Toast.makeText(getActivity(), "You have no Notification", Toast.LENGTH_SHORT).show();
            tip.setVisibility(View.GONE);
        }


        //Delete Item From List Start
        genNotiList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Notification")
                        .setMessage("Are you sure ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int delckr = dataBase.deleteGenRow(notificationData.get(position).id);

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
        });
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
            View v=inflater.inflate(R.layout.gennotilistview, parent, false);

            // ImageView iv = (ImageView)v.findViewById(R.id.cartimageView);
            TextView tv1=(TextView)v.findViewById(R.id.tv1);
            TextView tv2=(TextView)v.findViewById(R.id.tv2);

            GenNotiSetGet obj = notificationData.get(position);

            //Picasso.with(Notification.this).load(obj.getItemImg()).into(iv);
            tv1.setText(obj.getDate());
            tv2.setText(obj.getNotifcation());

            return v;
        }

    }
    //Adapter End
    

}