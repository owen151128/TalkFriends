package kr.pe.dreamer;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TalkFriendParseModule {
	private String baseUrl = null;
	private Document document = null;

	public TalkFriendParseModule(String scroll, String uid, String token) {
		this.baseUrl = String
				.format("http://api2.sntown.com/www/menu_people/list_page.php?user_id=%s&token=%s8&app_lang=ko"
						+ "&max_id=%s&opt_gender=2&opt_age_min=19&opt_age_max=25&force_opt_age_min=19&force_opt_age_max=70&"
						+ "opt_location=114&opt_city=0", uid, token, scroll);
	}

	public String[][] getList() {
		Elements elements = null;
		ArrayList<String> name = new ArrayList<>();
		ArrayList<String> age = new ArrayList<>();
		ArrayList<String> time = new ArrayList<>();
		ArrayList<String> article = new ArrayList<>();
		ArrayList<String> image = new ArrayList<>();
		ArrayList<String> timeId = new ArrayList<>();

		try {
			document = Jsoup.connect(baseUrl).get();
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

		elements = document.select("li"); // id_start
		for (Element e : elements) {
			timeId.add(e.attr("data-signin_date"));
		} // id_end

		String[][] result = { name.toArray(new String[name.size()]), age.toArray(new String[age.size()]),
				time.toArray(new String[time.size()]), article.toArray(new String[article.size()]),
				image.toArray(new String[image.size()]), timeId.toArray(new String[timeId.size()]) };
		return result;
	}
}
