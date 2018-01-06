package mindw.weather.spider;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mindw.weather.data.WeatherData;
import mindw.weather.data.WeatherItem;

/**
 * 爬取天气数据 http://www.tianqi.com/jiaxing/30/
 * 
 * @author mindw
 */
public class WeatherSpider {

	private static final Logger LOGGER = Logger.getLogger(WeatherSpider.class);

	/** 名字到拼音的映射 */
	private static Map<String, String> cityName2Pingyin;
	
	/** 拼音到名字的映射 */
	private static Map<String, String> pingyin2CityName;

	/** 缓存 */
	private static Map<String,WeatherData> cache;
	
	public static void active() {
		cityName2Pingyin = CitySpider.getCityNameAndPingyin();
		pingyin2CityName = new HashMap<>();
		cache = new HashMap<>();
		for(Map.Entry<String, String> e : cityName2Pingyin.entrySet()) {
			String cityName = e.getKey();
			String pingyin = e.getValue();
			cache.put(pingyin, null);
			pingyin2CityName.put(pingyin, cityName);
		}
	}
		
	/**
	 * 获得一个城市的天气
	 * 
	 * @param city
	 * @return
	 */
	public static WeatherData getWeatherItems(String queryName) {
		if(queryName==null||queryName.length()==0) {
			return WeatherData.ERROR_RESULT;
		}
		
		try {
			char ch = queryName.charAt(0);
			String city = null;
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
				// 英文直接查
				city = queryName.toLowerCase();
			} else {
				// 中文
				if (queryName.endsWith("市")) {
					queryName = queryName.substring(0, queryName.length() - 1);
				}
				// 中文转英文
				city = cityName2Pingyin.get(queryName);
			}
			
			// 判断city是否存在
			if(city==null||(!cache.containsKey(city))){
				return WeatherData.ERROR_RESULT;
			}
			
			// 判断是否在缓存中
			WeatherData pre = cache.get(city);
			String currentDate = currentDate();
			// 如果时间没有过期则返回
			if(pre!=null&&pre.getDate().equals(currentDate)){
				// System.out.println("缓存");
				return pre;
			}
			// System.out.println("没有命中");
			
			
			String url = String.format("http://www.tianqi.com/%s/30/", city);

			Document document = Jsoup.parse(new URL(url), 3000);

			Element rootElement = document.getElementsByClass("box_day").get(0);
			Elements elements = rootElement.getElementsByClass("table_day");

			// <h3><b>12月30日</b> <em>今天</em></h3>
			// <ul>
			// <li class="img"><img
			// src="http://pic9.tianqijun.com/static/tianqi2018/ico2/b8.png"></li>
			// <li class="temp">雨 4~<b>11</b>℃</li>
			// <li>西北风 2级</li>
			// </ul>

			List<WeatherItem> weatherItems = new ArrayList<>(elements.size());
			for (Element element : elements) {

				// 时间
				String timeStr = element.getElementsByTag("h3").get(0).text();
				String[] times = timeStr.trim().split(" ");
				String date = times[0];
				String dayKind = times[1];

				// 图片
				// http://pic9.tianqijun.com/static/tianqi2018/ico2/b8.png
				String weatherImg = element.getElementsByTag("img").attr("src");
				int index = weatherImg.lastIndexOf("/");
				weatherImg = weatherImg.substring(index + 1);

				// 雨 4~11℃
				String tempStr = element.getElementsByClass("temp").text();
				String[] temps = tempStr.split(" ");

				String weather = temps[0];
				String[] temperatures = temps[1].replaceAll("℃", "").split("~");

				String minTemperature = temperatures[0];
				String maxTemperature = temperatures[1];

				// 西北风 2级
				String wind = "";
				for (Element windElemnt : element.getElementsByTag("li")) {
					if (windElemnt.hasClass("img") || windElemnt.hasClass("temp")) {
						continue;
					}
					wind = windElemnt.text();
					break;
				}

				WeatherItem weatherItem = new WeatherItem(date, dayKind, weather, weatherImg, minTemperature,
						maxTemperature, wind);
				// System.out.println(weatherItem);
				weatherItems.add(weatherItem);
			}
			
			
			WeatherData weatherData = new WeatherData(pingyin2CityName.get(city), currentDate,weatherItems);
			cache.put(city,weatherData);
			return weatherData;

		} catch (Throwable e) {
			LOGGER.error(e.toString());
		}
		return WeatherData.ERROR_RESULT;
	}

	
	/***
	 * 获得当前时间 "2015-05-26"
	 */
    private static String currentDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
