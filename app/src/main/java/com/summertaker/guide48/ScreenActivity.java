package com.summertaker.guide48;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.summertaker.guide48.common.BaseApplication;
import com.summertaker.guide48.common.Config;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ScreenActivity extends AppCompatActivity {

    ArrayList<Member> mOshiMembers;

    ImageView mIvPicture;
    TextView mTvName;
    Button mBtnLeft;
    Button mBtnRight;

    String mMemberName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 잠금 화면 맨 위에 표시
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        // 애니메이션 설정
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.screen_activity);

        mOshiMembers = BaseApplication.getInstance().getOshimembers();

        mIvPicture = findViewById(R.id.ivPicture);
        mTvName = findViewById(R.id.tvName);

        mBtnLeft = findViewById(R.id.btnLeft);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                String text = btn.getText().toString();
                answerClick(text);
            }
        });

        mBtnRight = findViewById(R.id.btnRight);
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                String text = btn.getText().toString();
                answerClick(text);
            }
        });

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        run();
    }

    private void run() {
        Collections.shuffle(mOshiMembers);

        int mPosition = 0;
        Member member = mOshiMembers.get(mPosition);

        String fileName = Util.getUrlToFileName(member.getPictureUrl());
        File file = new File(Config.DATA_PATH, fileName);
        if (file.exists()) {
            Picasso.with(this).load(file).into(mIvPicture);
        } else {
            Picasso.with(this).load(member.getPictureUrl()).into(mIvPicture, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    //pbPictureLoading.setVisibility(View.GONE);
                    //imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    //pbPictureLoading.setVisibility(View.GONE);
                }
            });
        }

        mMemberName = member.getName();
        mTvName.setText(mMemberName);

        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < mOshiMembers.size(); i++) {
            if (i != mPosition) {
                indexList.add(i);
            }
        }
        Collections.shuffle(indexList);

        int randomIndex = indexList.get(0);
        String tempName = mOshiMembers.get(randomIndex).getName();

        if (randomIndex % 2 == 0) { // even
            mBtnLeft.setText(mMemberName);
            mBtnRight.setText(tempName);
        } else {
            mBtnLeft.setText(tempName);
            mBtnRight.setText(mMemberName);
        }
    }

    private void answerClick(String answer) {
        if (answer.equals(mMemberName)) {
            finish();
        } else {
            run();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
