package com.summertaker.guide48;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.summertaker.guide48.common.BaseActivity;
import com.summertaker.guide48.common.BaseApplication;
import com.summertaker.guide48.common.Config;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.member.GroupActivity;
import com.summertaker.guide48.member.OshiEditActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.Callback {

    private static final int REQUEST_PERMISSION_CODE = 100;

    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private MainAdapter mPagerAdapter;
    private ArrayList<Member> mOshiMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //mBaseToolbar.setTitle(mTitle + " (" + (position + 1) + "/" + mMemberList.size() + ")");
                //setAnswer(position);

                /*
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());
                if (fragment != null) {
                    MainFragment f = (MainFragment) fragment;
                    f.update();
                }
                */
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //----------------------------------------------------------------------------
        // 런타임에 권한 요청
        // https://developer.android.com/training/permissions/requesting.html?hl=ko
        //----------------------------------------------------------------------------
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                } else {
                    // permission denied
                    onPermissionDenied();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void onPermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("권한이 거부되었습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    void start() {
        mViewPager.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //goActivity();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //mGroupName = "";
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mOshiMembers = BaseApplication.getInstance().loadMember(Config.PREFERENCE_KEY_OSHIMEMBERS);
    }

    @Override
    public void onResume() {
        super.onResume();

        mOshiMembers = BaseApplication.getInstance().getOshimembers();
        Collections.shuffle(mOshiMembers);

        mPagerAdapter = null;
        mPagerAdapter = new MainAdapter(getSupportFragmentManager(), mOshiMembers);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, GroupActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(MainActivity.this, OshiEditActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goActivity() {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
        intent.putExtra("group", "");
        startActivity(intent);
        //startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e(mTag, "onActivityResult().resultCode: " + resultCode);

        //mOshiMembers = BaseApplication.getInstance().getOshimembers();
        //Collections.shuffle(mOshiMembers);
        //for (Member member : mOshiMembers) {
        //    Log.e(mTag, "member.getName(): " + member.getName());
        //}

        //mPagerAdapter = null;
        //mPagerAdapter = new MainAdapter(getSupportFragmentManager(), mOshiMembers);
        //mViewPager.setAdapter(mPagerAdapter);
        //mPagerAdapter.notifyDataSetChanged();

        //hideToolbarProgressBar();
        //if (resultCode == Activity.RESULT_OK) {
        //}
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onPrevSelected() {

    }

    @Override
    public void onNextSelected() {

    }

    @Override
    public void onError(String message) {

    }
}
