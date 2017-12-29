package com.summertaker.guide48.parser;

import android.util.Log;

import com.summertaker.guide48.data.Group;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.common.BaseParser;
import com.summertaker.guide48.data.Team;
import com.summertaker.guide48.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Hkt48Parser extends BaseParser {

    public void parse(String html, Group group, ArrayList<Team> teams, ArrayList<Member> members) {
        //Log.e(mTag, "html: " + html);

        /*
        <ul class="team_list">
            <li>
                <a href="/profile/?id=azuma_yuki">
                    <span class="photo_guard"></span>
                    <img src="../img/profile/n/azuma_yuki.jpg" alt="東由樹"/>
                    東由樹
                    <span class="english">YUKI AZUMA</span>
                </a>
            </li>
        */

        if (html == null || html.isEmpty()) {
            return;
        }

        //html = Util.getJapaneseString(html, "8859_1");

        Document doc = Jsoup.parse(html);

        Element root = doc.select("#main").first();
        if (root == null) {
            Log.e(mTag, "root is null...");
            return;
        }

        for (Element h1 : root.select("h1")) {
            String teamName;

            teamName = h1.text();

            Team team = new Team();
            team.setName(teamName);
            teams.add(team);

            Element ul = h1.nextElementSibling();
            for (Element li : ul.select("li")) {
                String name;
                String thumbnail;
                String picture;
                String url;

                Element a = li.select("a").first();
                url = a.attr("href");
                url = "http://sp.hkt48.jp/" + url;

                // http://cache.hkt48sp.qw.to/img/profile/member/15-thumb.jpg?cache=20171228090418
                // http://cache.hkt48sp.qw.to/img/profile/member/15-large.jpg?cache=20171228093645
                Element img = a.select("img").first();
                String src = img.attr("src");
                thumbnail = src;
                picture = src.replace("-thumb.", "-large.");

                Element n = li.select(".name").first();
                name = n.text();

                //Log.e(mTag, teamName + ", " + name + ", " + thumbnail + ", " + picture);

                Member member = new Member();
                member.setGroup(group.getName());
                member.setTeam(teamName);
                member.setName(name);
                member.setThumbnail(thumbnail);
                member.setPicture(picture);
                member.setUrl(url);

                members.add(member);
            }
        }
    }
}
