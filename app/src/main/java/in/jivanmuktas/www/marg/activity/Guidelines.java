package in.jivanmuktas.www.marg.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import in.jivanmuktas.www.marg.adapter.CustomExpandableListAdapter;
import in.jivanmuktas.www.marg.dataclass.GuideLineDoc;

import in.jivanmuktas.www.marg.R;

public class Guidelines extends BaseActivity {
    ExpandableListView guidelinesdata;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    TreeMap<String, List<String>> expandableListDetail;

    int listSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGuidelines);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getExtras().getString("TITLE");
        setTitle("Guidelines-"+title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        guidelinesdata = (ExpandableListView) findViewById(R.id.guidelinesdata);
        if(title.equals("Nivritti Gurukul")){
            expandableListDetail = GuideLineDoc.getDataNg();
        }else if(title.equals("Workshop")){
            expandableListDetail = GuideLineDoc.getDataWs();
        }else{
            expandableListDetail = GuideLineDoc.getDataGd();
        }

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        guidelinesdata.setAdapter(expandableListAdapter);
        listSize=expandableListTitle.size();
        guidelinesdata.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/

                ///////////////Code For When expand one collapse others
                for(int i = 0 ; i<listSize; i++){
                    if(i==groupPosition){
                        continue;
                    }
                    guidelinesdata.collapseGroup(i);
                }

            }
        });

        guidelinesdata.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        guidelinesdata.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
               /* Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();*/
                return false;
            }
        });
    }
}

