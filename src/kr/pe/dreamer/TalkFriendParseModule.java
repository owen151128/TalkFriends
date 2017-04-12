package kr.pe.dreamer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TalkFriendParseModule {
	private String baseUrl = null;
	private String info1 = null;
	private String info2 = null;
	private Document document = null;
	private Elements elements = null;
	private ArrayList<String> name = null;
	private ArrayList<String> age = null;
	private ArrayList<String> time = null;
	private ArrayList<String> article = null;
	private ArrayList<String> image = null;
	private ArrayList<String> timeId = null;
	private ArrayList<String> userId = null;
	private ArrayList<String> userInfo = null;

	public TalkFriendParseModule(String scroll, String uid, String token) {
		name = new ArrayList<>();
		age = new ArrayList<>();
		time = new ArrayList<>();
		article = new ArrayList<>();
		image = new ArrayList<>();
		timeId = new ArrayList<>();
		userId = new ArrayList<>();
		userInfo = new ArrayList<>();

		this.baseUrl = String
				.format("http://api2.sntown.com/www/menu_people/list_page.php?user_id=%s&token=%s8&app_lang=ko"
						+ "&max_id=%s&opt_gender=2&opt_age_min=19&opt_age_max=25&force_opt_age_min=19&force_opt_age_max=70&"
						+ "opt_location=114&opt_city=0", uid, token, scroll);
		this.info1 = "http://api2.sntown.com/www/menu_profile/user.php?"
				+ "file%3A%2F%2F%2Fandroid_asset%2Fwww%2Fmenu_profile%2Fuser.html=true"
				+ "&app_type=android&target_id=";
		this.info2 = String.format("&referer=&paid_view_photos=0&user_id=%s&token=%s&location=KR&lang=ko&app_ver=1.8.5",
				uid, token);

	}

	public String[][] getList() {
		try {
			document = Jsoup.connect(baseUrl).get();
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("/Users/dreamer/java_tmp"))));
			bw.write(document.toString());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		elements = document.select("table > tbody > tr > td > dl> dt > p > span.sn_name"); // name_start
		for (Element e : elements) {
			name.add(e.text());
		} // name_end

		elements = document.select("table > tbody > tr > td > dl> dt > p > span.n_info"); // age_start
		for (Element e : elements) {
			age.add(e.text());
		} // age_end

		elements = document.select("table > tbody > tr > td > dl> dt > p.float_right.n_info"); // time_start
		for (Element e : elements) {
			time.add(e.text());
		} // time_end

		elements = document.select("table > tbody > tr > td > dl> dd > p"); // article_start
		for (Element e : elements) {
			article.add(e.text());
		} // article_end

		elements = document.select("table > tbody > tr > td > div > p > img"); // image_start
		for (Element e : elements) {
			image.add(e.attr("src"));
		} // image_end

		elements = document.select("li"); // timeId_start
		for (Element e : elements) {
			timeId.add(e.attr("data-signin_date"));
			userId.add(e.attr("data-user_id"));
		} // timeId_end

		String[][] result = { name.toArray(new String[name.size()]), age.toArray(new String[age.size()]),
				time.toArray(new String[time.size()]), article.toArray(new String[article.size()]),
				image.toArray(new String[image.size()]), timeId.toArray(new String[timeId.size()]),
				userId.toArray(new String[userId.size()]) };

		return result;
	}

	public String[] getUserInfo(String id) {
		try { // userInfo_start

			document = Jsoup.connect(info1 + id + info2).get();
			elements = document.select("p");
			int integer = 0;
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).toString().contains("<p class=\"value\">"))
					integer = i;
			}

			for (int i = integer; i < elements.size(); i++) {
				userInfo.add(elements.get(i).text().toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} // userInfo_end

		return userInfo.toArray(new String[userInfo.size()]);
	}
}
