package mindw.weather.spider;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 爬取城市名称到拼音 
 * http://www.tianqi.com/chinacity.html
 * 
 * @author mindw
 */
public class CitySpider {

//	public static void main(String[] args) {
//		Map<String,String> cityName2Pingyin = CitySpider().getCityNameAndPingyin();
//		System.out.println(cityName2Pingyin);
//	}
	
	private static final Logger LOGGER = Logger.getLogger(CitySpider.class);

	/**
	 * 获得城市到拼音的映射
	 */
	public static Map<String,String> getCityNameAndPingyin() {
		try {

			String url = "http://www.tianqi.com/chinacity.html";

			Document document = Jsoup.parse(new URL(url), 5000);

			Element rootElement = document.getElementById("cityall");
			Elements elements = rootElement.getElementsByTag("li");

			Map<String,String> cityName2Pingyin = new HashMap<>();
			for (Element element : elements) {
				String cityName = element.text();
				String href = element.getElementsByTag("a").attr("href");
				if(href.startsWith("https:")) {
					// 前缀 https://www.tianqi.com/
					String pingyin = href.substring(23, href.length()-1);
					cityName2Pingyin.put(cityName, pingyin);
				}
			}
			LOGGER.info("成功获取cityName2Pingyin，size:"+cityName2Pingyin.size());
			return cityName2Pingyin;
		} catch (Throwable e) {
			LOGGER.error("获取cityName2Pingyin失败！");
			LOGGER.error(e.toString());
		}
		return new HashMap<>();
	}

}
