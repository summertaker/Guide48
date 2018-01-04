package com.summertaker.guide48.member;

import android.widget.CheckBox;

import com.summertaker.guide48.data.Member;

public interface OshiEditInterface {

    void onPictureClick(Member member);

    void onLikeClick(CheckBox checkBox, Member member);
}
