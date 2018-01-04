package com.summertaker.guide48;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.summertaker.guide48.common.BaseApplication;
import com.summertaker.guide48.common.BaseFragment;
import com.summertaker.guide48.common.Config;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.util.Util;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class MainFragment extends BaseFragment {

    private int mPosition;

    private Callback mCallback;

    ImageView mIvPicture;
    TextView mTvName;

    public interface Callback {
        void onPrevSelected();

        void onNextSelected();

        void onError(String message);
    }

    public MainFragment() {
    }

    public static MainFragment newInstance(int position) {
        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        mContext = inflater.getContext();
        //mLocale = Util.getLocaleStrng(mContext);

        mPosition = getArguments().getInt("position", 0);
        //final Member member = (Member) getArguments().getSerializable("member");

        mIvPicture = (ImageView) rootView.findViewById(R.id.ivPicture);
        mTvName = (TextView) rootView.findViewById(R.id.tvName);

        renderData();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;

            try {
                mCallback = (Callback) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement Listener for Fragment.");
            }
        }
    }

    public void renderData() {
        ArrayList<Member> members = BaseApplication.getInstance().getOshimembers();
        if (members.size() > mPosition) {
            final Member member = members.get(mPosition);
            if (member != null) {
                //Log.e(mTag, "picture: " + member.getPicture());
                String fileName = Util.getUrlToFileName(member.getPictureUrl());
                File file = new File(Config.DATA_PATH, fileName);
                if (file.exists()) {
                    Picasso.with(mContext).load(file).into(mIvPicture);
                } else {
                    Picasso.with(mContext).load(member.getPictureUrl()).into(mIvPicture, new com.squareup.picasso.Callback() {
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

                mTvName.setText(member.getName());
            }
        }
    }

    public void update() {
        Log.e(mTag, "update()..." + mPosition);
        //renderData();
    }
}

