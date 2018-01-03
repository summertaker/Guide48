package com.summertaker.guide48.member;

import android.widget.CheckBox;

import com.summertaker.guide48.data.Member;

public interface TeamInterface {

    void onPicutreClick(Member member);

    void onLikeClick(CheckBox checkBox, Member member);

    void onNameClick(Member member);
}
