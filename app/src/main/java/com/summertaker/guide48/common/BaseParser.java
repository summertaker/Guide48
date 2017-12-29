package com.summertaker.guide48.common;

import com.summertaker.guide48.data.Group;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.data.Team;

import java.util.ArrayList;

public class BaseParser {

    protected String mTag;

    public BaseParser() {
        mTag = "== " + this.getClass().getSimpleName();
    }

    public void parseTeam(String html, ArrayList<Team> teamList) {

    }

    public void parseMember(String html, Group group, Team team, ArrayList<Member> members) {

    }
}