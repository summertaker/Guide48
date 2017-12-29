package com.summertaker.guide48.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.summertaker.guide48.R;
import com.summertaker.guide48.data.Group;
import com.summertaker.guide48.data.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static final String TAG = BaseApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ArrayList<Group> mGroups = new ArrayList<>();
    private ArrayList<Member> mOshimembers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mGroups.add(new Group("AKB48", R.drawable.logo_akb48, "http://sp.akb48.co.jp/profile/", Config.USER_AGENT_MOBILE));
        mGroups.add(new Group("SKE48", R.drawable.logo_ske48, "http://spn.ske48.co.jp/profile/list.php", Config.USER_AGENT_MOBILE));
        mGroups.add(new Group("NMB48", R.drawable.logo_nmb48, "http://spn2.nmb48.com/profile/list.php", Config.USER_AGENT_MOBILE));
        mGroups.add(new Group("HKT48", R.drawable.logo_hkt48, "http://sp.hkt48.jp/qhkt48_list", Config.USER_AGENT_MOBILE));
        mGroups.add(new Group("NGT48", R.drawable.logo_ngt48, "http://ngt48.jp/profile", Config.USER_AGENT_DESKTOP));
        mGroups.add(new Group("STU48", R.drawable.logo_stu48, "http://www.stu48.com/feature/profile", Config.USER_AGENT_DESKTOP));

        mOshimembers = loadMember(Config.PREFERENCE_KEY_OSHIMEMBERS);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }

    public Group getGroup(String name) {
        Group group = null;
        for (Group data : mGroups) {
            if (data.getName().equals(name)) {
                group = data;
                break;
            }
        }

        return group;
    }

    public ArrayList<Member> getOshimembers() {
        return mOshimembers;
    }

    public void setmOshimembers(ArrayList<Member> members) {
        mOshimembers = members;
    }

    public ArrayList<Member> loadMember(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Config.USER_PREFERENCE_KEY, Context.MODE_PRIVATE);
        String jsonString = mSharedPreferences.getString(key, null);
        //Log.e(mTag, "jsonString: " + jsonString);

        ArrayList<Member> members = new ArrayList<>();

        if (jsonString != null) {
            JSONObject object = null;
            try {
                object = new JSONObject(jsonString);
                JSONArray array = object.getJSONArray("members");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Member m = new Member();
                    m.setGroup(obj.getString("group"));
                    m.setTeam(obj.getString("team"));
                    m.setName(obj.getString("name"));
                    m.setThumbnail(obj.getString("thumbnail"));
                    m.setPicture(obj.getString("picture"));
                    m.setUrl(obj.getString("url"));
                    m.setOshimember(obj.getBoolean("isOshimember"));
                    members.add(m);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return members;
    }

    public void saveMember(String key, ArrayList<Member> members) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Config.USER_PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor mSharedEditor = mSharedPreferences.edit();

        try {
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();

            for (Member member : members) {
                JSONObject o = new JSONObject();
                o.put("group", member.getGroup());
                o.put("team", member.getTeam());
                o.put("name", member.getName());
                o.put("thumbnail", member.getThumbnail());
                o.put("picture", member.getPicture());
                o.put("url", member.getUrl());
                o.put("isOshimember", member.isOshimember());
                array.put(o);
            }

            object.put("members", array);
            //Log.e(mTag, jsonObject.toString());

            mSharedEditor.putString(key, object.toString());
            mSharedEditor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
