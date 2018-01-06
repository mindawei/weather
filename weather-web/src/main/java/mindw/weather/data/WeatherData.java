package mindw.weather.data;

import java.util.ArrayList;
import java.util.List;

/***
 * 返回结果
 * 
 * @author mindw
 */
public class WeatherData {

	public static final WeatherData ERROR_RESULT = new WeatherData("未知区域", "", new ArrayList<>(0));

	private String queryName;

	/** 那一天的数据 2018-01-01 */
	private String date;

	private List<WeatherItem> weatherItems;

	public WeatherData() {
	}

	public WeatherData(String queryName, String date, List<WeatherItem> weatherItems) {
		super();
		this.queryName = queryName;
		this.date = date;
		this.weatherItems = weatherItems;
	}

	public List<WeatherItem> getWeatherItems() {
		return weatherItems;
	}

	public void setWeatherItems(List<WeatherItem> weatherItems) {
		this.weatherItems = weatherItems;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "WeatherData [queryName=" + queryName + ", date=" + date + ", weatherItems=" + weatherItems + "]";
	}

}
