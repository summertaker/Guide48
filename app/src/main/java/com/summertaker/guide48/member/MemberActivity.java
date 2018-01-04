package com.summertaker.guide48.member;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.squareup.picasso.Picasso;
import com.summertaker.guide48.R;
import com.summertaker.guide48.common.BaseActivity;
import com.summertaker.guide48.common.BaseApplication;
import com.summertaker.guide48.common.Config;
import com.summertaker.guide48.common.ImageViewerActivity;
import com.summertaker.guide48.common.WebDataAdapter;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.data.WebData;
import com.summertaker.guide48.parser.YahooParser;
import com.summertaker.guide48.util.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemberActivity extends BaseActivity {

    private Member mMember;

    private ArrayList<WebData> mYahooList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_activity);

        mContext = MemberActivity.this;

        mMember = BaseApplication.getInstance().getMember();

        initToolbar(mMember.getName());

        //ImageView ivPicture = findViewById(R.id.ivPicture);
        //Picasso.with(mContext).load(mMember.getPicture()).into(ivPicture);

        ImageView ivPicture;
        if (mMember.getGroupId().equals("akb48")) {
            ivPicture = findViewById(R.id.ivPictureCrop);
        } else {
            ivPicture = findViewById(R.id.ivPictureWrap);
        }
        ivPicture.setVisibility(View.VISIBLE);

        Picasso.with(mContext).load(mMember.getPictureUrl()).into(ivPicture);

        loadYahoo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadYahoo() {
        String query = mMember.getGroupName() + " " + mMember.getName().replace(" ", "");
        try {
            query = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "https://search.yahoo.co.jp/image/search?ei=UTF-8&p=" + query;

        requestData(url, Config.USER_AGENT_DESKTOP);
    }

    private void requestData(final String url, final String userAgent) {
        //Log.e(mTag, "url: " + url);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(mTag, "response:\n" + response);
                parseYahoo(url, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(mTag, "VolleyError: " + error.getMessage());
                Util.alert(mContext, getString(R.string.error), error.getMessage(), null);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", userAgent);
                return headers;
            }
        };

        BaseApplication.getInstance().addToRequestQueue(strReq, mTag);
    }

    private void parseYahoo(String url, String response) {
        YahooParser yahooParser = new YahooParser();
        yahooParser.parseImage(response, mYahooList);
        //Log.e(mTag, "mYahooList.size(): " + mYahooList.size());

        renderYahoo(url);
    }

    private void renderYahoo(final String url) {
        //mLoYahooLoading.setVisibility(View.GONE);

        LinearLayout loYahooLoading = findViewById(R.id.loYahooLoading);
        loYahooLoading.setVisibility(View.GONE);

        if (mYahooList.size() > 0) {
            WebDataAdapter adapter = new WebDataAdapter(mContext, R.layout.yahoo_item, 100, mYahooList);

            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gvYahoo);
            gridView.setExpanded(true);
            gridView.setFocusable(false);
            gridView.setAdapter(adapter);
            gridView.setVisibility(View.VISIBLE);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    WebData webData = (WebData) parent.getItemAtPosition(position);

                    String title = webData.getTitle();
                    String picture = webData.getPicture();
                    String url = webData.getUrl();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    //Intent intent = new Intent(MemberActivity.this, ImageViewerActivity.class);
                    //intent.putExtra("title", title);
                    //intent.putExtra("url", picture);
                    startActivity(intent);
                }
            });
        }
    }
}
