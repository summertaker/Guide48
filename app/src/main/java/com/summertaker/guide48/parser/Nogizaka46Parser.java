package com.summertaker.guide48.parser;

import com.summertaker.guide48.common.BaseParser;
import com.summertaker.guide48.data.Group;
import com.summertaker.guide48.data.Member;
import com.summertaker.guide48.data.Team;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Nogizaka46Parser extends BaseParser {

    public void parse(String html, Group group, ArrayList<Team> teams, ArrayList<Member> members) {
        /*
        <div class="main">
        	<h2>50音</h2>
	        <div id="member">
	            <ul class="clearfix">
	                <li>
	                    <a href="./detail/akimotomanatsu.php">
	                        <img src="/smph/member/img/akimotomanatsu_list.jpg?v2" alt="秋元 真夏" />
	                        <span class="heading">秋元 真夏</span>
	                        <span class="sub">あきもと まなつ</span>
	                        <!-- sub2 -->
	                    </a>
	                </li>
        */

        if (html == null || html.isEmpty()) {
            return;
        }

        //response = Util.getJapaneseString(response, "8859_1");

        Document doc = Jsoup.parse(html);
        Element root = doc.select(".main").first();

        if (root != null) {
            //Log.d(mTag, root.text());

            for (Element ul : root.select("ul")) {

                String teamName = group.getName();

                Element div = ul.previousElementSibling();
                if (div != null) {
                    teamName = div.text();
                }

                Team team = new Team();
                team.setName(teamName);
                teams.add(team);

                for (Element row : ul.select("li")) {
                    String name;
                    String thumbnail;
                    String picture;
                    String url;

                    Element el;

                    Element a = row.select("a").first();
                    url = a.attr("href");
                    url = url.replace("./", "/");
                    url = "http://www.nogizaka46.com/smph/member" + url;

                    Element img = a.select("img").first();
                    if (img == null) {
                        continue;
                    }

                    String src = img.attr("src");

                    // http://www.nogizaka46.com/smph/member/img/mukaihazuki_list.jpg?v2
                    src = "http://www.nogizaka46.com" + src;
                    thumbnail = src;

                    // http://img.nogizaka46.com/www/smph/member/img/nakamurareno_prof.jpg?v201303
                    src = src.replace("http://www.nogizaka46.com/smph/member/img/", "http://img.nogizaka46.com/www/smph/member/img/");
                    src = src.replace("_list.jpg", "_prof.jpg");
                    picture = src;

                    name = a.select(".heading").text();

                    //Log.e(mTag, name + " / " + thumbnailUrl + " / " + profileUrl);

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
}
