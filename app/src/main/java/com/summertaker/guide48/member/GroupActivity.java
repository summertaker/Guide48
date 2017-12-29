package com.summertaker.guide48.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.summertaker.guide48.R;
import com.summertaker.guide48.common.BaseActivity;
import com.summertaker.guide48.common.BaseApplication;
import com.summertaker.guide48.common.BaseParser;
import com.summertaker.guide48.data.Group;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.data.Team;
import com.summertaker.guide48.parser.Akb48Parser;
import com.summertaker.guide48.parser.Hkt48Parser;
import com.summertaker.guide48.parser.Ngt48Parser;
import com.summertaker.guide48.parser.Nmb48Parser;
import com.summertaker.guide48.parser.Ske48Parser;
import com.summertaker.guide48.parser.Stu48Parser;
import com.summertaker.guide48.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupActivity extends BaseActivity {

    private Group mGroup;

    private ArrayList<Group> mGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity);

        mContext = GroupActivity.this;

        //Intent intent = getIntent();
        //mGroupId = intent.getStringExtra("groupId");

        mGroups = BaseApplication.getInstance().getGroups();

        initToolbar(null);
        initToolbarProgressBar();

        initGridView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initGridView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GroupAdapter(mContext, mGroups));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGroup = (Group) parent.getItemAtPosition(position);
                //showToolbarProgressBar();
                //loadGroup();

                Intent intent = new Intent(mContext, TeamActivity.class);
                intent.putExtra("groupName", mGroup.getName());
                startActivityForResult(intent, 100);
            }
        });
    }
}
